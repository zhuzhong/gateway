package com.zz.gateway.exception;

import java.util.HashMap;
import java.util.Map;

public enum OauthErrorEnum {
	ERROR("HRT10000", "service unavailable"),
	GRANTTYPE("HRT10001", "grant_type is required"),
	APPID("HRT10002", "appid is required"),
	SECRET("HRT10003", "secret is required"),
	TIMSTAMP("HRT10004", "timestamp is required"),
	SIGN("HRT10005", "sign is required"),
	INVALID_SIGN("HRT10006", "invalid sign"),
	INVALID_REQUEST("HRT10007", "invalid request"),
	INVALID_CLIENT("HRT10008", "invalid appId"),
	INVALID_GRANT("HRT10009", "invalid grant"),
	UNAUTHORIZED_CLIENT("HRT10010", "unauthorized appId"),
	UNSUPPORTED_GRANT_TYPE("HRT10011", "unsupported grant_type"),
	INVALID_TOKEN("HRT10012", "invalid token"),
	ACCESS_DENIED("HRT10013", "access denied"),
	SERVICENAME("HRT10014", "service_name is required"),
	ACCESSTOKEN("HRT10015", "access_token is required"),
	INVALID_SERVICENAME("HRT10016", "invalid service_name"),
	CONTENTTYPE("HRT10017", "httprequest header content-type is required"),
	INVALID_CONTENTTYPE("HRT10018", "invalid content-type,just application/xml or application/json"),
	INVALID_SECRET("HRT10019", "invalid secret"),
	UN_VISIBLE_SERVICENAME("HRT10021", "service is not visible"),
	LOCK_ITEM_APPID("HRT10022", "current appid is locked"),
	LOCK_ITEM_API("HRT10023", "current service is locked"),
	APP_UNDEFIND_WHITE("HRT10024","undefind in whiteList"),
	SERVICE_UNDEFIND_WHITE("HRT10025","service_name undefind in whiteList"),
	NOT_CALLBACKURL("HRT10026","undefind in user's callBackUrl"),
	INTERFACE_FREQUENCY("HRT10027", "api freq out of limit")
	;
	// 成员变量
    private String errCode;
	private String errMsg;
    
    // 构造方法
    private OauthErrorEnum(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
    // 普通方法
    public static String getErrMsg(String errCode) {
        for (OauthErrorEnum c : OauthErrorEnum.values()) {
            if (c.getErrCode().equals(errCode)) {
                return c.getErrMsg();
            }
        }
        return null;
    }
    
    public static OauthErrorEnum getErr(String errCode) {
        for (OauthErrorEnum c : OauthErrorEnum.values()) {
            if (c.getErrCode().equals(errCode)) {
                return c;
            }
        }
        return null;
    }
    
    public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	private static Map<String,String> oauthErrorMap;
	static{
		oauthErrorMap = new HashMap<String,String>();
		oauthErrorMap.put("error", "HRT10000");
		oauthErrorMap.put("invalid_request", "HRT10007");
		oauthErrorMap.put("invalid_client", "HRT10008");
		oauthErrorMap.put("invalid_grant", "HRT10009");
		oauthErrorMap.put("unauthorized_client", "HRT10010");
		oauthErrorMap.put("401", "HRT10010");
		oauthErrorMap.put("unsupported_grant_type", "HRT10011");
		oauthErrorMap.put("invalid_token", "HRT10012");
		oauthErrorMap.put("access_denied", "HRT10013");
	}
	public static Map<String, String> getOauthErrorMap() {
		return oauthErrorMap;
	}
	public static void setOauthErrorMap(Map<String, String> oauthErrorMap) {
		OauthErrorEnum.oauthErrorMap = oauthErrorMap;
	}
}
