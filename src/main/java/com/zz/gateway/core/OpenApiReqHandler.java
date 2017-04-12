package com.zz.gateway.core;

import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.zz.gateway.exception.OauthErrorEnum;
import com.zz.gateway.exception.OpenApiException;
import com.zz.gateway.protocol.OpenApiContext;
import com.zz.gateway.protocol.OpenApiHttpRequestBean;
import com.zz.gateway.protocol.OpenApiHttpSessionBean;
import com.zz.gateway.service.ApiInterfaceService;
import com.zz.gateway.service.CacheService;
import com.zz.gateway.util.CommonCodeConstants;

public class OpenApiReqHandler extends OpenApiHandler {
   

    private final int maxReqDataLth = 500;

    
    private CacheService cacheService;
    private ApiInterfaceService apiInterfaceService;

    private ApiMultiHttpClientService mutiHttpClientUtil;
   
    
    
  /*  @SuppressWarnings("rawtypes")
    private String generatePassUrl(String targetUrl, String salt, String token, String timeStamp) {
        // 透传业务接口url参数
        StringBuilder sb = new StringBuilder();
        sb.append(targetUrl).append("?salt=").append(salt).append("&token=").append(token).append("&timestamp=")
                .append(timeStamp);
        return sb.toString();
    }*/

/*    private String getParams(String salt, String token, String timeStamp, Map<String, String> urlParams) {
        // 透传业务接口url参数
        StringBuilder sb = new StringBuilder();
        sb.append("salt=").append(salt).append("&token=").append(token).append("&timestamp=").append(timeStamp);
        String key = null;
        String value = null;
        if (null != urlParams) {
            Iterator entries = urlParams.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                key = (String) entry.getKey();
                value = (String) entry.getValue();
                sb.append("&").append(key).append("=").append(value);
            }
        }
        return sb.toString();
    }*/

  

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }




    public void setApiInterfaceService(ApiInterfaceService apiInterfaceService) {
        this.apiInterfaceService = apiInterfaceService;
    }




    public void setMutiHttpClientUtil(ApiMultiHttpClientService mutiHttpClientUtil) {
        this.mutiHttpClientUtil = mutiHttpClientUtil;
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
        String redisKey = request.getRedisKey();
        OpenApiRouteBean routeBean = (OpenApiRouteBean) cacheService.get(redisKey);
        
        AccessOpenApiDto obj = executeInvokeServce(routeBean, request);
        request.setServiceRsp(obj.getServiceRsp()); // 返回值
        log.info(String.format("doExecuteBiz执行end,request_id=%s", requestId));
        return false;
    }

    // step3
    private AccessOpenApiDto executeInvokeServce(OpenApiRouteBean routeBean, OpenApiHttpRequestBean request) {
        log.info("step3...");
        // 获得servcie请求数据
        routeBean.setServiceReqData(request.getServiceReqData());
        AccessOpenApiDto dto = doInvokeServce(routeBean);
        return dto;
    }

    // step4
    /**
     * 根据routeBean信息，调用后端服务，并获取相关的信息
     * 
     * @param bean
     * @return
     */
    private AccessOpenApiDto doInvokeServce(OpenApiRouteBean bean) {
        log.info("step4...");
        AccessOpenApiDto apiDto = new AccessOpenApiDto();
        apiDto.setAuthorized(true);
        String serviceRspData = serviceRoute(bean); // 后端服务返回值
        apiDto.setServiceRsp(serviceRspData);
        return apiDto;
    }

 

    /**
     * 根据routeBean信息，通过httpclient调用后端信息，然后将返回值构建成string
     * 
     * @param bean
     * @return
     */
    private String serviceRoute(OpenApiRouteBean bean) {
        log.info("step5...");
        String serviceRspData = null; // 后端服务返回值
        String reqData = bean.getServiceReqData(); // 请求的json格式数据参数
        String operationType = bean.getOperationType();
        if (operationType.equals(CommonCodeConstants.API_SYSERVICE_KEY)) {
            /*
             * Map<String, String> map = new TreeMap<String, String>();
             * map.put("code", "200"); map.put("message",
             * "The request has been accepted, the processing number is :" +
             * bean.getDataId()); serviceRspData = JSON.toJSONString(map);
             */
        } else if (operationType.equals(CommonCodeConstants.API_GETDATA_KEY)) {
            /*
             * ListOperations<String, String> lop = null; lop =
             * redisTemplate.opsForList(); while
             * (StringUtil.isNotEmpty(serviceRspData =
             * lop.rightPop(bean.getDataId()))) {
             * 
             * return JSON.parseObject(serviceRspData,
             * OpenApiRouteBeanVO.class).getServiceReqData(); }
             */
        } else {
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
                    serviceRspData = mutiHttpClientUtil.doHttpsPost(url, bean.getServiceReqData(), contentType);
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
                        serviceRspData = mutiHttpClientUtil.doPost(url, bean.getServiceReqData(), contentType);
                    }
                }
                if (ApiMutiHttpClientUtil.TIME_OUT_ERROR.equals(serviceRspData)) {
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
