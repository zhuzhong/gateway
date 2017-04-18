package com.aldb.gateway.core.support;

import java.util.Map;

import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aldb.gateway.core.AbstractOpenApiHandler;
import com.aldb.gateway.core.OpenApiRouteBean;
import com.aldb.gateway.exception.OpenApiException;
import com.aldb.gateway.exception.OpenApiServiceErrorEnum;
import com.aldb.gateway.protocol.OpenApiContext;
import com.aldb.gateway.protocol.OpenApiHttpRequestBean;
import com.aldb.gateway.protocol.OpenApiHttpSessionBean;
import com.aldb.gateway.service.CacheService;

public class OpenApiRspHandler extends AbstractOpenApiHandler {
    private static final Log logger = LogFactory.getLog(OpenApiRspHandler.class);

    /*
     * @Override public boolean execute(Context context) {
     * logger.info("step1----"); return doExcuteBiz(context); }
     */

    @Override
    public boolean doExcuteBiz(Context context) {
        logger.info("step2----");
        OpenApiContext blCtx = (OpenApiContext) context;
        OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) blCtx.getOpenApiHttpSessionBean();
        OpenApiHttpRequestBean request = httpSessionBean.getRequest();
        long currentTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.info(String.format("begin run doExecuteBiz,currentTime=%d,httpSessonBean=%s", currentTime,
                    httpSessionBean));
        }
        String printStr = this.executePrint(request);
        request.setPrintStr(printStr);

        if (logger.isDebugEnabled()) {
            logger.info(String.format("end run doExecuteBiz,currentTime=%d,elapase_time=%d milseconds,httpSessonBean=%s",
                    System.currentTimeMillis(), (System.currentTimeMillis() - currentTime) , httpSessionBean));
        }

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
            return "errrrrrrrrrrrrrr";
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
        }
        Map<String, String> httpHeader = bean.getReqHeader();
        String contentType = httpHeader.get(CONTENT_TYPE_KEY);

        if (contentType == null) {
            throw new OpenApiException(OpenApiServiceErrorEnum.PARA_NORULE_ERROR);
        }

        /*
         * if (body instanceof OpenApiException) { OpenApiException ex =
         * (OpenApiException) body; if
         * (ex.getErrorCode().equals(OauthErrorEnum.INVALID_TOKEN.getErrCode()))
         * { String userTokenKey = bean.getUserTokenRedisKey(); Cache redisCache
         * = this.cacheManager.getCache(openApiCacheName);
         * redisCache.put(userTokenKey, null); }
         * 
         * if (StringUtils.isBlank(contentType)) { return
         * XmlUtils.bean2xml((ex.getShortMsg(bean.getLogId()))); } if
         * (contentType.contains(CONTENT_TYPE_JSON)) { return
         * JSON.toJSONString(ex.getShortMsg(bean.getLogId())); } if
         * (contentType.contains(CONTENT_TYPE_XML)) { return
         * XmlUtils.bean2xml((ex.getShortMsg(bean.getLogId()))); } return
         * XmlUtils.bean2xml((ex.getShortMsg(bean.getLogId()))); }
         * 
         * if (body instanceof Exception) { OpenApiException ex = new
         * OpenApiException(OpenApiServiceErrorEnum.SYSTEM_BUSY);
         * ex.setStackTrace(((Exception) body).getStackTrace()); if
         * (contentType.contains(CONTENT_TYPE_JSON)) { return
         * JSON.toJSONString(ex.getShortMsg(bean.getLogId())); } if
         * (contentType.contains(CONTENT_TYPE_XML)) { return
         * XmlUtils.bean2xml((ex.getShortMsg(bean.getLogId()))); } }
         * 
         * if (body instanceof OauthTokenDto) { OauthTokenDto token =
         * (OauthTokenDto) body; if (contentType.contains(CONTENT_TYPE_JSON)) {
         * return JSON.toJSONString(token); } if
         * (contentType.contains(CONTENT_TYPE_XML)) { return
         * XmlUtils.bean2xml((token)); } }
         */

        return body.toString();
    }
}
