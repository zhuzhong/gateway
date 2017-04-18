package com.aldb.gateway.core.support;

import java.util.Map;

import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;

import com.aldb.gateway.core.AbstractOpenApiHandler;
import com.aldb.gateway.core.OpenApiRouteBean;
import com.aldb.gateway.exception.OauthErrorEnum;
import com.aldb.gateway.exception.OpenApiException;
import com.aldb.gateway.protocol.OpenApiContext;
import com.aldb.gateway.protocol.OpenApiHttpRequestBean;
import com.aldb.gateway.protocol.OpenApiHttpSessionBean;
import com.aldb.gateway.service.CacheService;

public class OpenApiReqAdapter extends AbstractOpenApiHandler {

    private CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public OpenApiReqAdapter() {
    }

    private OpenApiRouteBean initRouteBean(OpenApiHttpRequestBean request) {
        OpenApiRouteBean routeBean = null;
        logger.info("iniApiRouteBean，这一步可以校验token,当然这个根据我们的实际情况去实现");
        String accessToken = request.getAppToken();
        if (StringUtils.isBlank(accessToken)) {
            throw new OpenApiException(OauthErrorEnum.ACCESSTOKEN.getErrCode(), OauthErrorEnum.ACCESSTOKEN.getErrMsg());
        }
        logger.info("init 路由bean ");
        routeBean = new OpenApiRouteBean();
        routeBean.setReqId(request.getReqId()); // 内部请求id,利于跟踪
        routeBean.setApiId(request.getApiId());// 请求api_id
        routeBean.setVersion(request.getVersion());// api_version
        routeBean.setReqHeader(request.getReqHeader());// 请求头
        routeBean.setTimeStamp(request.getTimeStamp());// 请求时间

        routeBean.setOperationType(request.getOperationType()); // 请求操作类型
        routeBean.setRequestMethod(request.getRequestMethod());// 请求方法
        routeBean.setServiceReqData(request.getServiceReqData());// post请求参数
        // routeBean.setQueryString(request.getQueryString());// get请求参数
        routeBean.setServiceGetReqData(request.getServiceGetReqData()); // get请求参数
        if (request.getThdApiUrlParams() != null) {
            for (Map.Entry<String, String> maps : request.getThdApiUrlParams().entrySet()) {
                routeBean.addThdApiUrlParams(maps.getKey(), maps.getValue());
            }
        }
        cacheService.put(request.getRouteBeanKey(), routeBean);
        return routeBean;
    }

    // 路由参数的校验
    private void validateApiRouteParam(OpenApiHttpRequestBean routeBean) {
        logger.info("validateApiRouteParam方法是对路由参数的校验,但是现在我没有去实现");
    }

    private void validateParam(OpenApiHttpRequestBean request) {
        // 验证头信息
        // validateRequestHeader(request);
        // 验证业务字段
        validateApiRouteParam(request);
    }

    // step1
    private void setAuditContext(OpenApiHttpRequestBean request) {
        // 对于请求信息进行审计
        logger.info("setAuditContext设置审计的上下文信息...,我也没有实现");
    }

    @Override
    public boolean doExcuteBiz(Context context) {
        OpenApiContext openApiContext = (OpenApiContext) context;
        OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) openApiContext.getOpenApiHttpSessionBean();
        OpenApiHttpRequestBean request = httpSessionBean.getRequest();
        long currentTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.info(String.format("begin run doExecuteBiz,currentTime=%d,httpSessonBean=%s", currentTime,
                    httpSessionBean));
        }

        // 设置audit上下文参数
        setAuditContext(request);
        // 校验参数
        validateParam(request);
        initRouteBean(httpSessionBean.getRequest()); // 初始化路由bean
        if (logger.isDebugEnabled()) {
            logger.info(String.format(
                    "end run doExecuteBiz,currentTime=%d,elapase_time=%d milseconds,httpSessonBean=%s",
                    System.currentTimeMillis(), (System.currentTimeMillis() - currentTime), httpSessionBean));
        }

        if (StringUtils.isNotBlank(request.getPrintStr())) {
            return true;
        }
        return false;
    }
}
