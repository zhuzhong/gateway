/**
 * 
 */
package com.z.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;

import com.z.gateway.common.OpenApiHttpRequestBean;
import com.z.gateway.common.util.CommonCodeConstants;

/**
 * @author Administrator
 *
 */
public class OpenApiTokenParamValidateInterceptor extends AbstractOpenApiValidateInterceptor {

    @Override
    protected OpenApiHttpRequestBean iniOpenApiHttpRequestBean(HttpServletRequest request) {
        OpenApiHttpRequestBean bean = new OpenApiHttpRequestBean();
        bean.setOperationType(CommonCodeConstants.API_TOKEN_KEY);
        return bean;
    }

}
