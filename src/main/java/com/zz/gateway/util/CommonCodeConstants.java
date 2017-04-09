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

  
    
	/** 公共的请求参数
 	appId	String	16	是	打包app的唯一标识  not null 	
	apiId	String	64	是	API编码即api的唯一标识 not null 
	apiVersion	String	8	是	API版本号 not null 
	appToken	String	32	是	app授权令牌,用于授权  not null   	
	timeStamp	String	19	是	时间戳，格式为yyyy-mm-dd HH:mm:ss，时区为GMT+8 not null	
	signMethod	String	8	是	生成服务请求签名字符串所使用的算法类型，目前仅支持MD5， 
	sign	String	32	是	服务请求的签名字符串  not null 
	deviceToken	String	16	否	硬件标识token,app首次安装时发放的硬件唯一性标识
	userToken	String	16	否	用户token
	
	请求格式:
	1.对于post方法:
	{
		"publAttrs":{},
		"busiAttrs":{}
	}
	
	2.对于get方法:
		公共参数及业务参数直接串接在url地址上
		而相应的业务参数以parameter+阿拉伯数字进行编号
 */
    
    public static String pub_attrs="publAttrs";
    public static String busi_attrs="busiAttrs";
    public static String content_type="application/json";
    // 协议的公共参数
    public static String app_id="appId";
    public static String api_id = "apiId"; // 必须
    public static String version = "apiVersion";
    public static String app_token = "appToken";
    public static String time_stamp = "timeStamp";
    public static String sign_method="signMethod";
    public static String sign="sign";
    public static String device_token="deviceToken";
    public static String user_token="userToken";
    public static String format = "format";
    

    public static String getRouteBeanRedisKey(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(ROUTE_BEAN_KEY).append(CTRL_STR).append(key);
        return sb.toString();
    }

}
