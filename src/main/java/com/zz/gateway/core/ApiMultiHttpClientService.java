/**
 * 
 */
package com.zz.gateway.core;

import java.util.Map;

/**
 * @author Administrator
 *
 */
public interface ApiMultiHttpClientService {
    
    public  String doHttpsPost(String url, String content, String contentType) ;

    public  String doPost(String url, String reqData, String contentType) ;
    public  String doPost(String url, String reqData, String contentType,String params);
    
    public  Map<String,String> HttpGet(String webUrl, Map paramMap);
    
    public  Map<String,String> HttpGet(String url, String method, Map paramMap) ;
    
    public String HttpPost(String webUrl, Map paramMap) ;
    
    public String HttpPost(String url, String method, Map paramMap);
    

}