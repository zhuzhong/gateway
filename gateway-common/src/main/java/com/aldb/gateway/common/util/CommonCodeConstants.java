/**
 * 
 */
package com.aldb.gateway.common.util;

/**
 * @author Administrator
 *
 */
public class CommonCodeConstants {

    
    
    public static final String API_SERVICE_KEY = "openapi.service.HandlerExecuteTemplate";
    public static final String API_TOKEN_KEY = "openapi.gettoken.HandlerExecuteTemplate";
    public static final String API_WTSERVICE_KEY = "openapi.wtService";
    public static final String API_SYSERVICE_KEY = "openapi.syService";
    public static final String API_GETDATA_KEY = "openapi.getSyData";
    
    
    
    public static final String REQ_BEAN_KEY = "GATE_WAY_BEAN";

    
    
    public static final String ROUTE_BEAN_KEY = "BL_OPENAPI_ROUTE_BEAN";
    public static final String USER_TOKEN_KEY = "BL_OPENAPI_USER_TOKEN";
    public static final String USER_TOKEN_SYNC_SINGAL_KEY = "BL_OPENAPI_USER_TOKEN_SYNC_SINGAL";

    public static final String CTRL_STR = "_";
    public static final String ROUTE_APPID_KEY = "appId";
    public static final String ROUTE_TOKEN_KEY = "accessToken";
   
    public static final String SERVICE_TYPE_KEY = "servcieType";
    public static final String SERVICE_BODY_KEY = "body";
    public static final String SYSTEM_ERROR_KEY = "500";
    public static final String SERVICE_INVOKE_DATA = "servcieInvokeData";
    public static final String MDF_CHARSET_UTF_8 = "UTF-8";
    public static final String LOSE_MESSAGE_SENDSTATUS_UNSEND = "0";
    public static final String USER_LOGIN_KEY = "BL_OPENAPI_USER_LOGIN";
    
    public static final String TRACE_ID="traceId";

    /**
     * �������������
       appId   String  16  ��   ���app��Ψһ��ʶ  not null
       appToken    String  32  ��   app��Ȩ����,������Ȩ  not null     
       apiId   String  64  ��   API���뼴api��Ψһ��ʶ not null 
       apiVersion  String  8   ��   API�汾�� not null 
       timeStamp   String  19  ��   ʱ�������ʽΪyyyy-mm-dd HH:mm:ss��ʱ��ΪGMT+8 not null    
       signMethod  String  8   ��   ��ɷ�������ǩ���ַ���ʹ�õ��㷨���ͣ�Ŀǰ��֧��MD5�� 
       sign    String  32  ��   ���������ǩ���ַ�  not null 
       deviceToken String  16  ��   Ӳ����ʶtoken,app�״ΰ�װʱ���ŵ�Ӳ��Ψһ�Ա�ʶ
       userToken   String  16  ��   �û�token
     * 
     * �����ʽ: 
     * 1.����post����: { "publAttrs":{}, "busiAttrs":{} }
     * 2.����get����: ��������ҵ�����ֱ�Ӵ�����url��ַ�� ���ں��ʹ��restful��ʽ��get���ڽ��з���ע��ʱʹ�÷�����
     * user/1000 ��Ӧ��ע���ʽ user/{userId} 
     * Ȼ��ǰ������ʹ�õĲ�����ΪuserId,
     * ǰ�������ʽ GET
     * /serice.htm?userId=1000 
     * �����ʹ�÷�restful��ʽ��get����ֱ�ӽ�������url��ַ�ϾͿ�����
     */

    public static String pub_attrs = "publAttrs";
    public static String busi_attrs = "busiAttrs";
    public static String content_type = "application/json";
    // Э��Ĺ�������
    public static String app_id = "appId";
    public static String app_token = "appToken";
    public static String api_id = "service"; // ����
    public static String version = "version";
    public static String time_stamp = "timeStamp";
    public static String sign_method = "signMethod";
    public static String sign = "sign";
    public static String device_token = "deviceToken";
    public static String user_token = "userToken";
    public static String format = "format";

    public static String getRouteBeanRedisKey(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(ROUTE_BEAN_KEY).append(CTRL_STR).append(key);
        return sb.toString();
    }

    public static enum REQUEST_METHOD {
        POST, GET
    }
}
