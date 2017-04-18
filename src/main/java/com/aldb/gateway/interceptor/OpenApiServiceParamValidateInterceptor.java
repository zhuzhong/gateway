/**
 * 
 */
package com.aldb.gateway.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aldb.gateway.protocol.OpenApiHttpRequestBean;
import com.aldb.gateway.util.CommonCodeConstants;
import com.aldb.gateway.util.NetworkUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Administrator
 *
 */
public class OpenApiServiceParamValidateInterceptor extends AbstractOpenApiValidateInterceptor {

    private static final Log log = LogFactory.getLog(OpenApiServiceParamValidateInterceptor.class);

    /**
     * 根据请求的协议进行解析
     */
    @Override
    protected OpenApiHttpRequestBean iniOpenApiHttpRequestBean(HttpServletRequest request) {
        String requestMethod = request.getMethod();

        OpenApiHttpRequestBean bean = new OpenApiHttpRequestBean();
        if (requestMethod.equalsIgnoreCase(CommonCodeConstants.REQUEST_METHOD.POST.name())) {
            try {
                parsePostMethod(request, bean);
            } catch (IOException e) {
                log.error("这个请求格式不是application/json的,我处理不了...");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else if (requestMethod.equalsIgnoreCase(CommonCodeConstants.REQUEST_METHOD.GET.name())) {
            parseGetMethod(request, bean);
            bean.setQueryString(request.getQueryString());
        }
        bean.setThdApiUrlParams(extractThdUrlParams(request));
        bean.setLocalAddr(request.getLocalAddr());
        bean.setLocalPort(request.getLocalPort());
        bean.setClientAddr(NetworkUtil.getClientIpAddr(request));
        bean.setReqHeader(getHeadersInfo(request)); // 获取请求头
        if (request.getContentType() != null)
            bean.getReqHeader().put("content-type", request.getContentType());
        bean.setOperationType(CommonCodeConstants.API_SERVICE_KEY);
        bean.setRequestMethod(requestMethod);
        if (bean.getSignMethod() == null)
            bean.setSignMethod("MD5");
        if (bean.getFormat() == null)
            bean.setFormat("json");
        return bean;
    }

    private void parseGetMethod(HttpServletRequest request, OpenApiHttpRequestBean bean) {

        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String mapJson = enums.nextElement();
            String value = (String) request.getParameter(mapJson);
            // 公共参数
            // app_id
            if (mapJson.equals(CommonCodeConstants.app_id)) {
                bean.setAppId(value);
            } else if (mapJson.equals(CommonCodeConstants.api_id)) {
                bean.setApiId(value); // api_id
            } else if (mapJson.equals(CommonCodeConstants.version)) {
                bean.setVersion(value);// version

            } else if (mapJson.equals(CommonCodeConstants.app_token)) {
                bean.setAppToken(value); // app_token
            } else if (mapJson.equals(CommonCodeConstants.time_stamp)) {
                bean.setTimeStamp(value); // time_stamp
            } else if (mapJson.equals(CommonCodeConstants.sign_method)) {
                bean.setSignMethod(value); // sign_method
            }
            /*
             * else{ bean.setSignMethod("MD5"); //sign_method默认值 }
             */

            else if (mapJson.equals(CommonCodeConstants.sign)) {
                bean.setSign(value); // sign

            } else if (mapJson.equals(CommonCodeConstants.device_token)) {
                bean.setDeviceToken(value); // device_token

            } else if (mapJson.equals(CommonCodeConstants.user_token)) {
                bean.setUserToken(value);
            } // user_token

            else if (mapJson.equals(CommonCodeConstants.format)) {
                bean.setFormat(value);
            }
            /*
             * else{ bean.setFormat("json"); //业务请求参数默认是json格式 }
             */
            else {
                bean.addServiceGetReqData(mapJson, value);
            }
        }
    }

    private void parsePostMethod(HttpServletRequest request, OpenApiHttpRequestBean bean) throws IOException {

        String contentType = request.getContentType();
        if (CommonCodeConstants.content_type.equalsIgnoreCase(contentType)) { // 是son格式的，我们能够处理

            int len = request.getContentLength();
            ServletInputStream iii = request.getInputStream();
            byte[] buffer = new byte[len];
            iii.read(buffer, 0, len);
            String bodyContent = new String(buffer, "UTF-8"); // 将请求流读出来，利用　json
                                                              // 框架解析出来

            JSONObject jsonObject = JSONObject.parseObject(bodyContent);
            if (jsonObject.containsKey(CommonCodeConstants.pub_attrs)) {
                JSONObject jsonObject2 = jsonObject.getJSONObject(CommonCodeConstants.pub_attrs);
                Map mapJson = (Map) jsonObject2;
                // 公共参数
                // app_id
                if (mapJson.get(CommonCodeConstants.app_id) != null) {
                    bean.setAppId(mapJson.get(CommonCodeConstants.app_id).toString());
                }
                if (mapJson.get(CommonCodeConstants.api_id) != null)
                    bean.setApiId(mapJson.get(CommonCodeConstants.api_id).toString()); // api_id
                if (mapJson.get(CommonCodeConstants.version) != null)
                    bean.setVersion(mapJson.get(CommonCodeConstants.version).toString());// version

                if (mapJson.get(CommonCodeConstants.app_token) != null)
                    bean.setAppToken(mapJson.get(CommonCodeConstants.app_token).toString()); // app_token
                if (mapJson.get(CommonCodeConstants.time_stamp) != null)
                    bean.setTimeStamp(mapJson.get(CommonCodeConstants.time_stamp).toString()); // time_stamp
                if (mapJson.get(CommonCodeConstants.sign_method) != null) {
                    bean.setSignMethod(mapJson.get(CommonCodeConstants.sign_method).toString()); // sign_method
                } else {
                    bean.setSignMethod("MD5"); // sign_method默认值
                }

                if (mapJson.get(CommonCodeConstants.sign) != null)
                    bean.setSign(mapJson.get(CommonCodeConstants.sign).toString()); // sign

                if (mapJson.get(CommonCodeConstants.device_token) != null)
                    bean.setDeviceToken(mapJson.get(CommonCodeConstants.device_token).toString()); // device_token

                if (mapJson.get(CommonCodeConstants.user_token) != null) {
                    bean.setUserToken(mapJson.get(CommonCodeConstants.user_token).toString());
                } // user_token

                if (mapJson.get(CommonCodeConstants.format) != null) {
                    bean.setFormat(mapJson.get(CommonCodeConstants.format).toString());
                } else {
                    bean.setFormat("json"); // 业务请求参数默认是json格式
                }
                String reqData = jsonObject.getJSONObject(CommonCodeConstants.busi_attrs).toJSONString();
                bean.setServiceReqData(reqData); // 请求体
            }

        }

    }

    private Map<String, String> extractThdUrlParams(HttpServletRequest request) {
        Map<String, String> urlParams = new HashMap<String, String>();
        Map<String, String[]> orignalUrlParams = request.getParameterMap();
        String key = null;
        String[] values = null;
        if (null != orignalUrlParams) {
            Iterator entries = orignalUrlParams.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                key = (String) entry.getKey();
                values = (String[]) entry.getValue();
                if (key.equals(CommonCodeConstants.app_id) || key.equals(CommonCodeConstants.api_id)
                        || key.equals(CommonCodeConstants.version) || key.equals(CommonCodeConstants.app_token)
                        || key.equals(CommonCodeConstants.time_stamp) || key.equals(CommonCodeConstants.sign_method)
                        || key.equals(CommonCodeConstants.sign) || key.equals(CommonCodeConstants.device_token)
                        || key.equals(CommonCodeConstants.user_token) || key.equals(CommonCodeConstants.format)) {
                    continue;
                }
                String val = values[0];
                try {
                    val = java.net.URLEncoder.encode(val, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    log.error("exception on prceeding chinese char: " + val + " with " + e.getMessage());
                }

                urlParams.put(key, null != values ? val : "");
            }
        }
        return urlParams;
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    /*
     * private String getHttpRequestBodyString(HttpServletRequest request) { if
     * ("POST".equalsIgnoreCase(request.getMethod())) { Scanner s = null; try {
     * s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A"); }
     * catch (IOException e) { log.error(e.getMessage()); } return s.hasNext() ?
     * s.next() : ""; } return ""; }
     */
}
