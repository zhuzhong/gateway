/**
 * 
 */
package com.zz.gateway.protocol;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class OpenApiHttpSessionBean implements Serializable{

    private OpenApiHttpRequestBean request;

    /**
     * @param reqBean
     */
    public OpenApiHttpSessionBean(OpenApiHttpRequestBean reqBean) {
        this.request = reqBean;
    }

    public OpenApiHttpRequestBean getRequest() {
        return request;
    }

    public void setRequest(OpenApiHttpRequestBean request) {
        this.request = request;
    }

}
