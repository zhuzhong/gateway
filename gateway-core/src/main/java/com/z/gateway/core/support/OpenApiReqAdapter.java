package com.z.gateway.core.support;

import java.util.Map;

import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;

import com.z.gateway.common.OpenApiHttpRequestBean;
import com.z.gateway.common.exception.OauthErrorEnum;
import com.z.gateway.common.resp.CommonResponse;
import com.z.gateway.core.AbstractOpenApiHandler;
import com.z.gateway.core.OpenApiRouteBean;
import com.z.gateway.protocol.OpenApiContext;
import com.z.gateway.protocol.OpenApiHttpSessionBean;
import com.z.gateway.service.AuthenticationService;
import com.z.gateway.service.CacheService;

public class OpenApiReqAdapter extends AbstractOpenApiHandler {

	public OpenApiReqAdapter() {
	}

	protected OpenApiRouteBean initRouteBean(OpenApiHttpRequestBean request) {

		logger.info("init 路由bean ,using openapihttprequestbean={}", request);
		OpenApiRouteBean routeBean = null;
		routeBean = new OpenApiRouteBean();
		if (request.getTraceId() != null)
			routeBean.setTraceId(request.getTraceId()); // 内部请求id,利于跟踪
		if (request.getApiId() != null)
			routeBean.setApiId(request.getApiId());// 请求api_id
		if (request.getVersion() != null)
			routeBean.setVersion(request.getVersion());// api_version
		if (request.getReqHeader() != null)
			routeBean.setReqHeader(request.getReqHeader());// 请求头
		if (request.getTimeStamp() != null)
			routeBean.setTimeStamp(request.getTimeStamp());// 请求时间
		if (request.getOperationType() != null)
			routeBean.setOperationType(request.getOperationType()); // 请求操作类型
		if (request.getRequestMethod() != null)
			routeBean.setRequestMethod(request.getRequestMethod());// 请求方法
		if (request.getServiceReqData() != null)
			routeBean.setServiceReqData(request.getServiceReqData());// post请求参数
		// routeBean.setQueryString(request.getQueryString());// get请求参数
		if (request.getServiceGetReqData() != null)
			routeBean.setServiceGetReqData(request.getServiceGetReqData()); // get请求参数
		if (request.getThdApiUrlParams() != null) {
			for (Map.Entry<String, String> maps : request.getThdApiUrlParams().entrySet()) {
				routeBean.addThdApiUrlParams(maps.getKey(), maps.getValue());
			}
		}
		cacheService.put(request.getRouteBeanKey(), routeBean);
		return routeBean;
	}

	private CacheService cacheService;

	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	private void setError(String errorCode, String errMsg, OpenApiHttpRequestBean requestBean) {
		CommonResponse<String> r = new CommonResponse<String>(false);
		r.setErrorCode(errorCode);
		r.setErrorMsg(errMsg);
		requestBean.setPrintStr(r.toString());
	}

	protected void validateParam(OpenApiHttpRequestBean requestBean) {
		/*
		 * String appId = requestBean.getAppId(); if
		 * (StringUtils.isBlank(appId)) { // appId为空
		 * setError(OauthErrorEnum.APP_ID.getErrCode(),
		 * OauthErrorEnum.APP_ID.getErrMsg(), requestBean); return; } String
		 * appToken = requestBean.getAppToken();
		 * 
		 * if (StringUtils.isBlank(appToken)) {// appToken为空
		 * setError(OauthErrorEnum.APP_TOKEN.getErrCode(),
		 * OauthErrorEnum.APP_TOKEN.getErrMsg(), requestBean); return; }
		 */
		if (StringUtils.isBlank(requestBean.getApiId())) {
			setError(OauthErrorEnum.API_ID.getErrCode(), OauthErrorEnum.API_ID.getErrMsg(), requestBean);
			return;
		}

		if (StringUtils.isBlank(requestBean.getTimeStamp())) {
			setError(OauthErrorEnum.TIMSTAMP.getErrCode(), OauthErrorEnum.TIMSTAMP.getErrMsg(), requestBean);
			return;
		}
		/*
		 * String accessToken = requestBean.getAppToken(); if
		 * (StringUtils.isBlank(accessToken)) { throw new
		 * OpenApiException(OauthErrorEnum.ACCESSTOKEN.getErrCode(),
		 * OauthErrorEnum.ACCESSTOKEN.getErrMsg()); }
		 */
	}

	protected void authRequestBean(OpenApiHttpRequestBean requestBean) {
		// 对于请求信息进行审计
		logger.info("authRequestBean权限校验...");
		if (this.authenticationService != null) {
			this.authenticationService.doAuthOpenApiHttpRequestBean(requestBean);
		}
	}

	@Override
	protected boolean doExcuteBiz(Context context) {
		OpenApiContext openApiContext = (OpenApiContext) context;
		OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) openApiContext.getOpenApiHttpSessionBean();
		OpenApiHttpRequestBean request = httpSessionBean.getRequest();
		long currentTime = System.currentTimeMillis();

		logger.debug("begin  run doExectuteBiz,currentTImd={},httpSessionBean={}", currentTime, httpSessionBean);

		// 参数校验
		validateParam(request);
		// 权限校验
		authRequestBean(request);

		//initRouteBean(httpSessionBean.getRequest()); // 初始化路由bean
		
		logger.debug("end rn doExecuteBiz,currentTime={},elaspsed_time={} milseconds,httpSessionBean={}",System.currentTimeMillis(),(System.currentTimeMillis() - currentTime), httpSessionBean);

		if (StringUtils.isNotBlank(request.getPrintStr())) {
			return true;
		}
		return false;
	}

	// 外部依赖的服务--------------------------------------
	private AuthenticationService authenticationService;

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
}
