/**
 * 
 */
package com.zz.gateway.interceptor;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.zz.gateway.protocol.OpenApiHttpRequestBean;
import com.zz.gateway.util.CommonCodeConstants;
import com.zz.gateway.util.NetworkUtil;

/**
 * @author Administrator
 *
 */
public class GateWayServiceParamValidateInterceptor extends
		OpenApiValidateInterceptor {

	private static final Log log = LogFactory
			.getLog(GateWayServiceParamValidateInterceptor.class);

	/**
	 * 这个就是请求的协议，这个实现只支 application/json格式的请求，对于非json的不处理
	 */
	@Override
	protected OpenApiHttpRequestBean iniOpenApiHttpRequestBean(
			HttpServletRequest request) {
		String requestMethod = request.getMethod();

		OpenApiHttpRequestBean bean = new OpenApiHttpRequestBean();
		if (requestMethod.equalsIgnoreCase("post")) {
			try {
				parsePostMethod(request, bean);
			} catch (IOException e) {
				log.error("这个请求格式不是application/json的,我处理不了...");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		if (requestMethod.equalsIgnoreCase("get")) {
			parseGetMethod(request, bean);
		}

		bean.setOperationType(CommonCodeConstants.API_SERVICE_KEY);
		return bean;
	}

	private void parseGetMethod(HttpServletRequest request,
			OpenApiHttpRequestBean bean) {

	}

	private void parsePostMethod(HttpServletRequest request,
			OpenApiHttpRequestBean bean) throws IOException {

		String contentType = null;
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			contentType = request.getContentType();
		}

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
				//app_id
				if(mapJson.get(CommonCodeConstants.app_id)!=null){
					bean.setAppId(mapJson.get(CommonCodeConstants.app_id).toString());
				}
				if (mapJson.get(CommonCodeConstants.api_id) != null)
					bean.setApiId(mapJson.get(CommonCodeConstants.api_id)
							.toString()); // api_id
				if (mapJson.get(CommonCodeConstants.version) != null)
					bean.setVersion(mapJson.get(CommonCodeConstants.version)
							.toString());//version
				
				if (mapJson.get(CommonCodeConstants.app_token) != null)
					bean.setAppToken(mapJson.get(CommonCodeConstants.app_token)
							.toString()); //app_token
				if (mapJson.get(CommonCodeConstants.time_stamp) != null)
					bean.setTimeStamp(mapJson.get(
							CommonCodeConstants.time_stamp).toString()); //time_stamp
				if (mapJson.get(CommonCodeConstants.sign_method) != null){
					bean.setSignMethod(mapJson.get(
							CommonCodeConstants.sign_method).toString()); //sign_method
				}else{
					bean.setSignMethod("MD5"); //sign_method默认值
				}
				
				if (mapJson.get(CommonCodeConstants.sign) != null)
					bean.setSign(mapJson.get(CommonCodeConstants.sign)
							.toString()); //sign

				if (mapJson.get(CommonCodeConstants.device_token) != null)
					bean.setDeviceToken(mapJson.get(CommonCodeConstants.device_token)
							.toString()); //device_token
				
				if(mapJson.get(CommonCodeConstants.user_token)!=null){
					bean.setUserToken(mapJson.get(CommonCodeConstants.user_token).toString());
				} //user_token
				
				if (mapJson.get(CommonCodeConstants.format) != null){
					bean.setFormat(mapJson.get(CommonCodeConstants.format)
							.toString());
				}else{
					bean.setFormat("json"); //业务请求参数默认是json格式
				}
			
			

				bean.setReqHeader(getHeadersInfo(request)); // 获取请求头
				String reqData = jsonObject.getJSONObject(CommonCodeConstants.busi_attrs)
						.toJSONString();
				bean.setServiceReqData(reqData); // 请求体
				// bean.setServiceReqData(getHttpRequestBodyString(request)); //
				// 请求体

				bean.setLocalAddr(request.getLocalAddr());
				bean.setLocalPort(request.getLocalPort());
				bean.setClientAddr(NetworkUtil.getClientIpAddr(request));

			}

		}

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
