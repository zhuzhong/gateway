/**
 * 
 */
package com.aldb.gateway.core;

import java.util.Map;


/**
 * @author Administrator
 *
 */
public interface OpenApiHttpClientService {

    // get请求
    public String doGet(String webUrl);
    public String doGet(String webUrl, Map<String,String> paramMap);
    public String doHttpsGet(String webUrl);
    
    public String doHttpsGet(String webUrl, Map<String,String> paramMap);
    
/*    public Map<String, String> HttpGet(String webUrl, Map paramMap);

    public Map<String, String> HttpGet(String url, String method, Map paramMap);
    */
    public String doHttpsPost(String url, String content, String contentType);

    public String doPost(String url, String reqData, String contentType);

    /*public String doPost(String url, String reqData, String contentType, String params);
    public String HttpPost(String webUrl, Map paramMap);

    public String HttpPost(String url, String method, Map paramMap);
*/
}