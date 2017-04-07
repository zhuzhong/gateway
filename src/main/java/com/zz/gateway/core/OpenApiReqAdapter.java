package com.zz.gateway.core;

import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.zz.gateway.exception.OauthErrorEnum;
import com.zz.gateway.exception.OpenApiException;
import com.zz.gateway.protocol.OpenApiContext;
import com.zz.gateway.protocol.OpenApiHttpRequestBean;
import com.zz.gateway.protocol.OpenApiHttpSessionBean;
import com.zz.gateway.service.CacheService;

public class OpenApiReqAdapter extends OpenApiHandler {

    /*
     * private ApiInterfaceService apiRouteService;
     * 
     * private OauthService oauthService;
     * 
     * private ApiUsersService apiUsersService;
     * 
     * private ApiUserInterfaceService apiUserInterfaceService;
     * 
     * private String desKey;
     */

    private CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public OpenApiReqAdapter() {
    }

    private OpenApiRouteBean iniBean(OpenApiHttpRequestBean request) {
        OpenApiRouteBean bean = null;
        // 初始化service_route_bean
        bean = iniApiRouteBean(request);

        cacheService.put(request.getRedisKey(), bean);
        return bean;
    }

    private OpenApiRouteBean iniApiRouteBean(OpenApiHttpRequestBean request) {
        log.info("iniApiRouteBean，这一步可以校验token,当然这个根据我们的实际情况去实同");
        String accessToken = request.getAppToken();
        if (StringUtils.isBlank(accessToken)) {
            throw new OpenApiException(OauthErrorEnum.ACCESSTOKEN.getErrCode(), OauthErrorEnum.ACCESSTOKEN.getErrMsg());
        }
        log.info("init 路由bean ");
        OpenApiRouteBean bean = new OpenApiRouteBean();
        bean.setApiId(request.getApiId());
        bean.setReqHeader(request.getReqHeader());
        bean.setTimeStamp(request.getTimeStamp());
        bean.setReqId(request.getReqId());
        bean.setOperationType(request.getOperationType());
        bean.setServiceReqData(request.getServiceReqData());
        return bean;
    }

    // 路由参数的校验
    private void validateApiRouteParam(OpenApiHttpRequestBean routeBean) {
        log.info("validateApiRouteParam方法是对路由参数的校验,但是现在我没有去实现");
    }

    private void validateParam(OpenApiHttpRequestBean request) {
        // 验证头信息
        // validateRequestHeader(request);
        // 验证业务字段
        validateApiRouteParam(request);
    }

    @Override
    public boolean execute(Context context) {
        return doExcuteBiz(context);
    }

    @Override
    public boolean doExcuteBiz(Context context) {
        OpenApiContext blCtx = (OpenApiContext) context;
        OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) blCtx.getOpenApiHttpSessionBean();
        OpenApiHttpRequestBean request = httpSessionBean.getRequest();
        String requestId = httpSessionBean.getRequest().getReqId();
        log.info(String.format("doExecuteBiz执行begin,request_id=%s，相应的request为%s", requestId, JSON.toJSONString(request)));
        // 设置audit上下文参数
        setAuditContext(request); //
        // 校验参数
        validateParam(request); // 参数校验
        iniBean(httpSessionBean.getRequest());
        // 根据accessToken加载appid并维护到routebean,放入redis
        // resetRouteBean(request);
        log.info(String.format("doExecuteBiz执行end,request_id=%s", requestId));
        return false;
    }
}
