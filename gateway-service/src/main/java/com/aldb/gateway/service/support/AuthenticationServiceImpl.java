package com.aldb.gateway.service.support;

import com.aldb.gateway.common.OpenApiHttpRequestBean;
import com.aldb.gateway.common.exception.OauthErrorEnum;
import com.aldb.gateway.common.resp.CommonResponse;
import com.aldb.gateway.regist.api.ApiInfoService;
import com.aldb.gateway.regist.api.bean.ApiInfo;
import com.aldb.gateway.service.AuthenticationService;
import com.aldb.gateway.sub.api.ApiSubscriptionService;
import com.aldb.gateway.sub.api.AppSubscriptionService;
import com.aldb.gateway.sub.api.bean.ApiSubscription;
import com.aldb.gateway.sub.api.bean.AppSubscription;

public class AuthenticationServiceImpl implements AuthenticationService {

    private ApiInfoService apiInfoService; // api信息服务类

    private AppSubscriptionService appSubscriptionService;

    private ApiSubscriptionService apiSubscriptionService;

    public void setApiInfoService(ApiInfoService apiInfoService) {
        this.apiInfoService = apiInfoService;
    }

    public void setAppSubscriptionService(AppSubscriptionService appSubscriptionService) {
        this.appSubscriptionService = appSubscriptionService;
    }

    public void setApiSubscriptionService(ApiSubscriptionService apiSubscriptionService) {
        this.apiSubscriptionService = apiSubscriptionService;
    }

    /**
     * 鉴定该接口是否对于该app进行授权
     */
    @Override
    public void doAuthOpenApiHttpRequestBean(OpenApiHttpRequestBean requestBean) {
        String apiId = requestBean.getApiId();
        String apiVersion = requestBean.getVersion();
        ApiInfo apiInfo = apiInfoService.queryApiInfoByIdAndVersion(apiId, apiVersion);
        if (apiInfo == null) {
            // api没有上线
            setError(OauthErrorEnum.ERROR.getErrCode(), OauthErrorEnum.ERROR.getErrMsg(), requestBean);
            return;
        }
        String appId = requestBean.getAppId();
        String appToken = requestBean.getAppToken();

        AppSubscription appSubscription = appSubscriptionService.queryAppSubscriptionByIdAndToken(appId, appToken);
        if (appSubscription == null) {// 应用没有注册
            setError(OauthErrorEnum.INVALID_CLIENT.getErrCode(), OauthErrorEnum.INVALID_CLIENT.getErrMsg(), requestBean);
            return;
        }
        ApiSubscription apiSubscription = apiSubscriptionService.queryApiSubscriptionByAppIdAndApiId(
                appSubscription.getId(), apiInfo.getId());
        if (apiSubscription == null) { // 该应用没有订阅该api
            setError(OauthErrorEnum.UNAUTHORIZED_CLIENT.getErrCode(), OauthErrorEnum.UNAUTHORIZED_CLIENT.getErrMsg(),
                    requestBean);
            return;
        }
    }

    private void setError(String errorCode, String errMsg, OpenApiHttpRequestBean requestBean) {
        CommonResponse<String> r = new CommonResponse<String>(false);
        r.setErrorCode(errorCode);
        r.setErrorMsg(errMsg);
        requestBean.setPrintStr(r.toString());
    }
}
