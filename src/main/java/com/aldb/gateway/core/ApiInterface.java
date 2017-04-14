/**
 * 
 */
package com.aldb.gateway.core;

/**
 * 后端服务接口信息
 * 
 * @author Administrator
 *
 */
public class ApiInterface {

    private String targetUrl; //后端接口服务地址
    
    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

}
