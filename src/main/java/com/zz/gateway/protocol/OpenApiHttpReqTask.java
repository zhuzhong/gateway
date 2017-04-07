package com.zz.gateway.protocol;

import com.zz.gateway.handler.OpenApiHandlerExecuteTemplate;

public class OpenApiHttpReqTask extends AbstractTask {
    private OpenApiHttpSessionBean httpSessionBean;
    private OpenApiHandlerExecuteTemplate handlerExecuteTemplate;

    public OpenApiHttpReqTask(OpenApiHttpSessionBean httpSessionBean,
            OpenApiHandlerExecuteTemplate handlerExecuteTemplate) {
        this.httpSessionBean = httpSessionBean;
        this.handlerExecuteTemplate = handlerExecuteTemplate;
    }

    public OpenApiHttpSessionBean getHttpSessionBean() {
        return httpSessionBean;
    }

    public void setHttpSessionBean(OpenApiHttpSessionBean httpSessionBean) {
        this.httpSessionBean = httpSessionBean;
    }

    public OpenApiHandlerExecuteTemplate getHandlerExecuteTemplate() {
        return handlerExecuteTemplate;
    }

    public void setHandlerExecuteTemplate(OpenApiHandlerExecuteTemplate handlerExecuteTemplate) {
        this.handlerExecuteTemplate = handlerExecuteTemplate;
    }

    @Override
    public Object doBussiness() throws Exception {
        OpenApiContext blCtx = new OpenApiContext();
        blCtx.setOpenApiHttpSessionBean(httpSessionBean);
        this.handlerExecuteTemplate.execute(blCtx);
        return blCtx.getOpenApiHttpSessionBean();
    }
}
