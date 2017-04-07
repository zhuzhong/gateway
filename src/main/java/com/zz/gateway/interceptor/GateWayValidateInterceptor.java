/**
 * 
 */
package com.zz.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zz.gateway.protocol.OpenApiHttpRequestBean;
import com.zz.gateway.util.CommonCodeConstants;

/**请求拦截器
 * @author Administrator
 *
 */
public abstract class GateWayValidateInterceptor implements HandlerInterceptor {

   
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub
        
    }

    
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub
        
    }

  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        // 初始化请求bean
        OpenApiHttpRequestBean reqBean = iniOpenApiHttpRequestBean(request);
        request.setAttribute(CommonCodeConstants.REQ_BEAN_KEY, reqBean);
        return true;
    }
    
    protected abstract OpenApiHttpRequestBean iniOpenApiHttpRequestBean(
            HttpServletRequest request);

}
