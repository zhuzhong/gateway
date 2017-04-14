/**
 * 
 */
package com.aldb.gateway.core;

/**
 * @author Administrator
 *
 */
public class AccessOpenApiDto {

    private String serviceRsp;  //服务响应值

    private boolean isAuthorized;//是否授权
    
    public boolean isAuthorized() {
        return isAuthorized;
    }


    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    
    
    public String getServiceRsp() {
        return serviceRsp;
    }

    public void setServiceRsp(String serviceRsp) {
        this.serviceRsp = serviceRsp;
    }

}
