package com.z.gateway.core;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z.gateway.common.OpenApiHttpRequestBean;
import com.z.gateway.common.exception.OauthErrorEnum;
import com.z.gateway.common.exception.OpenApiException;

public abstract class AbstractOpenApiHandler implements Command {

	protected static Logger logger = LoggerFactory.getLogger(AbstractOpenApiHandler.class);

	// public String accessServiceUri;

	// public String accessTokenUri;

	// public String openApiCacheName;

	protected final String CONTENT_TYPE_KEY = "content-type";
	protected final String CONTENT_TYPE_XML = "application/xml";
	protected final String CONTENT_TYPE_JSON = "application/json";
	protected final String HEADER_HOST_KEY = "host";
	protected final String HEADER_SERVER_KEY = "server";

	/*
	 * public OauthErrorEnum getBlErrorObj(String errorCode) { String
	 * blErrorCode = OauthErrorEnum.getOauthErrorMap().get(errorCode);
	 * OauthErrorEnum error = null; if (StringUtils.isBlank(blErrorCode)) {
	 * error = OauthErrorEnum.ACCESS_DENIED; } else { error =
	 * OauthErrorEnum.getErr(blErrorCode); } return error; }
	 */

	protected Map<String, String> getHeadersInfo(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}

		return map;
	}

	protected void validateRequestHeader(OpenApiHttpRequestBean routeBean) {
		String contentType = routeBean.getReqHeader().get(CONTENT_TYPE_KEY);
		if (StringUtils.isBlank(contentType)) {
			throw new OpenApiException(OauthErrorEnum.CONTENTTYPE.getErrCode(), OauthErrorEnum.CONTENTTYPE.getErrMsg());
		}
		if (!contentType.contains(CONTENT_TYPE_JSON) && !contentType.contains(CONTENT_TYPE_XML)) {
			throw new OpenApiException(OauthErrorEnum.INVALID_CONTENTTYPE.getErrCode(),
					OauthErrorEnum.INVALID_CONTENTTYPE.getErrMsg());
		}
	}

	// step1
	@Override
	public boolean execute(Context context) {

		return doExcuteBiz(context);
	}

	protected abstract boolean doExcuteBiz(Context context);

	/*
	 * protected CacheService cacheService = new DefaultCacheServiceImpl(); //
	 * 饿汉模式，注入一个默认的
	 * 
	 * public void setCacheService(CacheService cacheService) {
	 * this.cacheService = cacheService; }
	 */
}
