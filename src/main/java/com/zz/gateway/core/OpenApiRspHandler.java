package com.zz.gateway.core;

import java.util.Map;

import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.zz.gateway.exception.OpenApiException;
import com.zz.gateway.exception.OpenApiServiceErrorEnum;
import com.zz.gateway.protocol.OpenApiContext;
import com.zz.gateway.protocol.OpenApiHttpRequestBean;
import com.zz.gateway.protocol.OpenApiHttpSessionBean;
import com.zz.gateway.service.CacheService;

public class OpenApiRspHandler extends OpenApiHandler {
    private static final Log logger = LogFactory.getLog(OpenApiRspHandler.class);

  /*  @Override
    public boolean execute(Context context) {
        logger.info("step1----");
        return doExcuteBiz(context);
    }*/

    @Override
    public boolean doExcuteBiz(Context context) {
        log.info("step2----");
        OpenApiContext blCtx = (OpenApiContext) context;
        OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) blCtx.getOpenApiHttpSessionBean();
        OpenApiHttpRequestBean request = httpSessionBean.getRequest();
        String requestId = httpSessionBean.getRequest().getReqId();
        log.info(String.format("doExecuteBiz执行begin,request_id=%s，相应的request为%s", requestId, JSON.toJSONString(request)));
        String printStr = this.executePrint(request);
        request.setPrintStr(printStr);
        log.info(String.format("doExecuteBiz执行end,request_id=%s", requestId));
        return false;
    }

    private CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    private String executePrint(OpenApiHttpRequestBean request) {
        log.info("step3...");
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
            String redisKey = request.getRedisKey();
            if (StringUtils.isNotBlank(redisKey)) {
                cacheService.remove(redisKey);
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
        log.info("step4....");
        Object body = (Object) bean.getServiceRsp();
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
