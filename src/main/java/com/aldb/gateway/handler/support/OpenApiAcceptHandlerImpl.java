/**
 * 
 */
package com.aldb.gateway.handler.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.aldb.gateway.handler.OpenApiAcceptHandler;
import com.aldb.gateway.handler.OpenApiHandlerExecuteTemplate;
import com.aldb.gateway.handler.ThreadPoolHandler;
import com.aldb.gateway.protocol.OpenApiHttpReqTask;
import com.aldb.gateway.protocol.OpenApiHttpRequestBean;
import com.aldb.gateway.protocol.OpenApiHttpSessionBean;
import com.aldb.gateway.service.IdService;
import com.aldb.gateway.util.CommonCodeConstants;
import com.aldb.gateway.util.OpenApiResponseUtils;

/**
 * @author Administrator
 *
 */
@Service
public class OpenApiAcceptHandlerImpl implements OpenApiAcceptHandler {

    private static Log logger = LogFactory.getLog(OpenApiAcceptHandlerImpl.class);
    @Autowired
    private IdService idGenerator;

    @Override
    public void acceptRequest(HttpServletRequest request, HttpServletResponse response) {

        OpenApiHttpRequestBean reqBean = (OpenApiHttpRequestBean) request
                .getAttribute(CommonCodeConstants.REQ_BEAN_KEY);
        String reqId = idGenerator.genInnerRequestId();
        reqBean.setReqId(reqId);
        request.setAttribute(CommonCodeConstants.REQ_BEAN_KEY, reqBean); // 重新设置bean
        // 将当前请求放入线程池处理，若超过线程池最大处理数则抛出reach queue max deepth 异常
        addTask2Pool(response, new OpenApiHttpSessionBean(reqBean));
    }

    @Autowired
    @Qualifier("serviceHandlerExecuteTemplate")
    private OpenApiHandlerExecuteTemplate serviceHandlerExecuteTemplate;

    @Autowired
    private ThreadPoolHandler poolHandler;

    private void addTask2Pool(HttpServletResponse response, OpenApiHttpSessionBean sessionBean) {
        logger.info("added one task to thread pool");
        OpenApiHttpReqTask task = null;
        String operationType = sessionBean.getRequest().getOperationType();
        if (CommonCodeConstants.API_SERVICE_KEY.equals(operationType)) {
            task = new OpenApiHttpReqTask(sessionBean, this.serviceHandlerExecuteTemplate);
        }

        /**
         * 走责任链，将相关的请求处理
         */
        OpenApiHttpSessionBean tmp = (OpenApiHttpSessionBean) poolHandler.addTask(task);
        // 写入响应
        OpenApiResponseUtils.writeRsp(response, tmp.getRequest());
    }

}
