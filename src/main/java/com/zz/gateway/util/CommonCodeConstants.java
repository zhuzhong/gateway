/**
 * 
 */
package com.zz.gateway.util;

/**
 * @author Administrator
 *
 */
public class CommonCodeConstants {

    public static final String REQ_BEAN_KEY = "GATE_WAY_BEAN";

    public static final String API_SERVICE_KEY = "service";

    public static final String ROUTE_BEAN_KEY = "BL_OPENAPI_ROUTE_BEAN";
    public static final String USER_TOKEN_KEY = "BL_OPENAPI_USER_TOKEN";
    public static final String USER_TOKEN_SYNC_SINGAL_KEY = "BL_OPENAPI_USER_TOKEN_SYNC_SINGAL";

    public static final String CTRL_STR = "_";
    public static final String ROUTE_APPID_KEY = "appId";
    public static final String ROUTE_TOKEN_KEY = "accessToken";
    public static final String API_TOKEN_KEY = "bl.openapi.oauth.gettoken";

    public static final String API_WTSERVICE_KEY = "wtService";
    public static final String API_SYSERVICE_KEY = "syService";
    public static final String API_GETDATA_KEY = "getSyData";
    public static final String SERVICE_TYPE_KEY = "servcieType";
    public static final String SERVICE_BODY_KEY = "body";
    public static final String SYSTEM_ERROR_KEY = "500";
    public static final String SERVICE_INVOKE_DATA = "servcieInvokeData";
    public static final String MDF_CHARSET_UTF_8 = "UTF-8";
    public static final String LOSE_MESSAGE_SENDSTATUS_UNSEND = "0";
    public static final String USER_LOGIN_KEY = "BL_OPENAPI_USER_LOGIN";

    // 协议的公共参数

    public static String api_id = "api_id"; // 必须
    public static String app_token = "app_token";
    public static String format = "format";
    public static String time_stamp = "time_stamp";
    public static String version = "version";
    

    public static String getRouteBeanRedisKey(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(ROUTE_BEAN_KEY).append(CTRL_STR).append(key);
        return sb.toString();
    }

}
