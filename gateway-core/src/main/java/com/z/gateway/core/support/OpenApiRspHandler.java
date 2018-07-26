package com.z.gateway.core.support;

import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z.gateway.common.OpenApiHttpRequestBean;
import com.z.gateway.common.exception.OpenApiException;
import com.z.gateway.common.exception.OpenApiServiceErrorEnum;
import com.z.gateway.core.AbstractOpenApiHandler;
import com.z.gateway.core.OpenApiRouteBean;
import com.z.gateway.protocol.OpenApiContext;
import com.z.gateway.protocol.OpenApiHttpSessionBean;
import com.z.gateway.service.CacheService;


@Deprecated
public class OpenApiRspHandler extends AbstractOpenApiHandler {
	private static final Logger logger = LoggerFactory.getLogger(OpenApiRspHandler.class);

	/*
	 * @Override public boolean execute(Context context) {
	 * logger.info("step1----"); return doExcuteBiz(context); }
	 */

	@Override
	protected boolean doExcuteBiz(Context context) {
		logger.info("step2----");
		OpenApiContext blCtx = (OpenApiContext) context;
		OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) blCtx.getOpenApiHttpSessionBean();
		OpenApiHttpRequestBean request = httpSessionBean.getRequest();
		long currentTime = System.currentTimeMillis();
		
			logger.debug("begin run doExecuteBiz,currentTime={},httpSessonBean={}", currentTime,
					httpSessionBean);
		
		/*String printStr = this.executePrint(request);
		request.setPrintStr(printStr);*/

		
			logger.debug(
					"end run doExecuteBiz,currentTime={},elapase_time={} milseconds,httpSessonBean={}",
							System.currentTimeMillis(), (System.currentTimeMillis() - currentTime), httpSessionBean);
		

		return false;
	}

	private CacheService cacheService;

	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	private String executePrint(OpenApiHttpRequestBean request) {
		logger.info("step3...");
		try {
			return this.getResponseBody(request);
		} catch (Exception e) {
			OpenApiException ex = null;
			if (e instanceof OpenApiException) {
				ex = (OpenApiException) e;
			} else {
				ex = new OpenApiException(OpenApiServiceErrorEnum.SYSTEM_BUSY, e.getCause());
			}
			logger.error("executePrint error, " + e.getMessage());
			// return XmlUtils.bean2xml((ex.getShortMsg("unknow")));
			return "error";
		} finally {
			// 从redis移除当前routebean
			String routeBeanKey = request.getRouteBeanKey();
			if (StringUtils.isNotBlank(routeBeanKey)) {
				cacheService.remove(routeBeanKey);
			}

			/*
			 * // 设置同步信号unlock redisKey =
			 * request.getUserTokenSyncSingalRedisKey(); if
			 * (StringUtils.isNotBlank(redisKey)) { Cache redisCache =
			 * this.cacheManager.getCache(openApiCacheName);
			 * redisCache.put(request.getUserTokenSyncSingalRedisKey(),
			 * CommonCodeConstants.SyncSingalType.SingalUnLock.getCode()); }
			 */
		}

	}

	private String getResponseBody(OpenApiHttpRequestBean bean) {
		logger.info("step4....");
		String routeBeanKey = bean.getRouteBeanKey();
		OpenApiRouteBean routeBean = (OpenApiRouteBean) cacheService.get(routeBeanKey);
		Object body = (Object) routeBean.getServiceRsp();
		if (body instanceof String) {
			return body.toString();
		} else {
			throw new RuntimeException("返回内容格式不对...");
		}

	}
}
