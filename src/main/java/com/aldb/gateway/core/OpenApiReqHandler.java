package com.aldb.gateway.core;

import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;

import com.aldb.gateway.exception.OauthErrorEnum;
import com.aldb.gateway.exception.OpenApiException;
import com.aldb.gateway.protocol.OpenApiContext;
import com.aldb.gateway.protocol.OpenApiHttpRequestBean;
import com.aldb.gateway.protocol.OpenApiHttpSessionBean;
import com.aldb.gateway.service.ApiInterfaceService;
import com.aldb.gateway.service.CacheService;
import com.aldb.gateway.util.CommonCodeConstants;
import com.aldb.gateway.util.UrlUtil;
import com.alibaba.fastjson.JSON;

public class OpenApiReqHandler extends OpenApiHandler {

    private final int maxReqDataLth = 500;

    private CacheService cacheService;
    private ApiInterfaceService apiInterfaceService;

    private ApiHttpClientService apiHttpClientService;

    /*
     * @SuppressWarnings("rawtypes") private String generatePassUrl(String
     * targetUrl, String salt, String token, String timeStamp) { // 透传业务接口url参数
     * StringBuilder sb = new StringBuilder();
     * sb.append(targetUrl).append("?salt="
     * ).append(salt).append("&token=").append(token).append("&timestamp=")
     * .append(timeStamp); return sb.toString(); }
     */

    /*
     * private String getParams(String salt, String token, String timeStamp,
     * Map<String, String> urlParams) { // 透传业务接口url参数 StringBuilder sb = new
     * StringBuilder();
     * sb.append("salt=").append(salt).append("&token=").append(
     * token).append("&timestamp=").append(timeStamp); String key = null; String
     * value = null; if (null != urlParams) { Iterator entries =
     * urlParams.entrySet().iterator(); while (entries.hasNext()) { Map.Entry
     * entry = (Map.Entry) entries.next(); key = (String) entry.getKey(); value
     * = (String) entry.getValue();
     * sb.append("&").append(key).append("=").append(value); } } return
     * sb.toString(); }
     */

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setApiInterfaceService(ApiInterfaceService apiInterfaceService) {
        this.apiInterfaceService = apiInterfaceService;
    }

    public void setApiHttpClientService(ApiHttpClientService apiHttpClientService) {
        this.apiHttpClientService = apiHttpClientService;
    }

    // step2
    @Override
    public boolean doExcuteBiz(Context context) {
        log.info("step2...");
        OpenApiContext openApiContext = (OpenApiContext) context;
        OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) openApiContext.getOpenApiHttpSessionBean();
        OpenApiHttpRequestBean request = httpSessionBean.getRequest();
        String requestId = request.getReqId();
        log.info(String.format("doExecuteBiz执行begin,request_id=%s，相应的request为%s", requestId, JSON.toJSONString(request)));
        String routeBeanKey = request.getRouteBeanKey();
        OpenApiRouteBean routeBean = (OpenApiRouteBean) cacheService.get(routeBeanKey);

        routeBean.setServiceRsp(doInvokeBackService(routeBean)); // 返回值
        log.info(String.format("doExecuteBiz执行end,request_id=%s,执行后的request为%s", requestId, JSON.toJSONString(request)));
        return false;
    }

    /**
     * 根据routeBean信息，通过httpclient调用后端信息，然后将返回值构建成string
     * 
     * @param bean
     * @return
     */
    private String doInvokeBackService(OpenApiRouteBean bean) {
        log.info("step5...");
        String serviceRspData = null; // 后端服务返回值
        String operationType = bean.getOperationType();
        String requestMethod = bean.getRequestMethod();

        if (operationType.equals(CommonCodeConstants.API_SYSERVICE_KEY)) {
            /*
             * Map<String, String> map = new TreeMap<String, String>();
             * map.put("code", "200"); map.put("message",
             * "The request has been accepted, the processing number is :" +
             * bean.getDataId()); serviceRspData = JSON.toJSONString(map);
             */
        } else if (CommonCodeConstants.API_GETDATA_KEY.equals(operationType)) {
            /*
             * ListOperations<String, String> lop = null; lop =
             * redisTemplate.opsForList(); while
             * (StringUtil.isNotEmpty(serviceRspData =
             * lop.rightPop(bean.getDataId()))) {
             * 
             * return JSON.parseObject(serviceRspData,
             * OpenApiRouteBeanVO.class).getServiceReqData(); }
             */
        } else if (CommonCodeConstants.API_SERVICE_KEY.equals(operationType)) {
            log.info(String.format("{serviceId:%s ,version:%s }", bean.getApiId(), bean.getVersion()));
            ApiInterface apiInfo = apiInterfaceService.findOne(bean.getApiId(), bean.getVersion());
            if (apiInfo == null) {
                return String.format("该apiId=%s,version=%s已下线", bean.getApiId(), bean.getVersion());
            }
            if (CommonCodeConstants.REQUEST_METHOD.GET.name().equalsIgnoreCase(requestMethod)) { // get请求
                String url = apiInfo.getUrl();
                url = UrlUtil.dealUrl(url, bean.getThdApiUrlParams());
                if (StringUtils.isNotBlank(bean.getQueryString())) {
                    url += "?" + bean.getQueryString(); // 串好url 地址
                }
                log.info(String.format("{service url:%s} ", url));
                // String contentType =
                // bean.getReqHeader().get(CONTENT_TYPE_KEY);
                if (url.startsWith("https")) {
                    serviceRspData = apiHttpClientService.doHttpsGet(url);
                } else {
                    serviceRspData = apiHttpClientService.doGet(url);
                }

            } else if (CommonCodeConstants.REQUEST_METHOD.POST.name().equalsIgnoreCase(requestMethod)) {// post请求
                String url = apiInfo.getUrl();
                log.info(String.format("{service url:%s} ", url));
                String reqData = bean.getServiceReqData(); // 请求的json格式数据参数
                if (StringUtils.isNotBlank(reqData) && reqData.length() > this.maxReqDataLth) {
                    reqData = reqData.substring(0, this.maxReqDataLth - 1);
                }
                log.info(String.format("{serviceId:%s ,reqData:%s }", bean.getApiId(), reqData));

                String contentType = bean.getReqHeader().get(CONTENT_TYPE_KEY);
                if (url.startsWith("https://")) {
                    serviceRspData = apiHttpClientService.doHttpsPost(url, bean.getServiceReqData(), contentType);
                } else {
                    serviceRspData = apiHttpClientService.doPost(url, bean.getServiceReqData(), contentType);
                }
                if ("timeout".equals(serviceRspData)) {
                    log.error("invoke service: response is null!");
                    throw new OpenApiException(OauthErrorEnum.ERROR.getErrCode(), OauthErrorEnum.ERROR.getErrMsg());
                }
            }
        } else {
            String reqData = bean.getServiceReqData(); // 请求的json格式数据参数
            if (StringUtils.isNotBlank(reqData) && reqData.length() > this.maxReqDataLth) {
                reqData = reqData.substring(0, this.maxReqDataLth - 1);
            }
            log.info(String.format("{serviceId:%s ,reqData:%s }", bean.getApiId(), reqData));

            ApiInterface apiInfo = apiInterfaceService.findOne(bean.getApiId(), bean.getVersion());
            String contentType = bean.getReqHeader().get(CONTENT_TYPE_KEY);

            try {
                String url = apiInfo.getTargetUrl();
                log.info("{service url: " + url);
                if (url.startsWith("https://")) {
                    serviceRspData = apiHttpClientService.doHttpsPost(url, bean.getServiceReqData(), contentType);
                } else {
                    if (bean.getApiId().equals("bl.kd100.tms.backstatus")
                            || bean.getApiId().equals("bl.order.dc.tmsexpress")) {
                        /*
                         * url = this.generatePassUrl(apiInfo.getTargetUrl(),
                         * user.getSalt(), bean.getAccessToken(),
                         * bean.getTimeStamp()); String params =
                         * this.getParams(user.getSalt(), bean.getAccessToken(),
                         * bean.getTimeStamp(), bean.getThdApiUrlParams());
                         * serviceRspData = mutiHttpClientUtil.doPost(url,
                         * bean.getServiceReqData(), contentType, params);
                         */
                    } else {
                        serviceRspData = apiHttpClientService.doPost(url, bean.getServiceReqData(), contentType);
                    }
                }
                if ("timeout".equals(serviceRspData)) {
                    log.error("invoke service: response is null!");
                    throw new OpenApiException(OauthErrorEnum.ERROR.getErrCode(), OauthErrorEnum.ERROR.getErrMsg());
                }
            } catch (Exception e) {
                log.error("invoke service: " + e.getMessage());
                throw new OpenApiException(OauthErrorEnum.ERROR.getErrCode(), OauthErrorEnum.ERROR.getErrMsg());
            }
        }
        return serviceRspData;
    }
}
