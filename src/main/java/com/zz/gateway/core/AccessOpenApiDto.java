/**
 * 
 */
package com.zz.gateway.core;

/**
 * @author Administrator
 *
 */
public class AccessOpenApiDto {

    private String serviceRsp;  //��˷��񷵻�ֵ

    private boolean isAuthorized;//�Ƿ�����֤
    
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
