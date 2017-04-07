/**
 * 
 */
package com.zz.gateway.handler.support;

import java.util.concurrent.FutureTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.zz.gateway.exception.OpenApiException;
import com.zz.gateway.exception.OpenApiServiceErrorEnum;
import com.zz.gateway.handler.ThreadPoolHandler;
import com.zz.gateway.protocol.AbstractTask;

/**
 * @author Administrator
 *
 */
@Service
public class ThreadPoolHandlerImpl implements ThreadPoolHandler {

    private static Log logger = LogFactory.getLog(ThreadPoolHandlerImpl.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public Object addTask(AbstractTask task) {
        try {
            FutureTask<Object> tsFutre = new FutureTask<Object>(task);
            taskExecutor.execute(tsFutre);
            while (!tsFutre.isDone()) {
                try {
                    logger.debug("waitting for result");
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    logger.error("exception happend on executing task with " + e.getMessage());
                }
            }
            return tsFutre.get();
        } catch (TaskRejectedException e) {
            logger.error("the queue reached max deepth");
            throw new OpenApiException(OpenApiServiceErrorEnum.SYSTEM_QUEUE_DEEPTH);
        } catch (Throwable e) {
            Throwable throwable = e.getCause();
            if (throwable instanceof OpenApiException) {
                throw (OpenApiException) throwable;
            }
            logger.error("exception happend on executing task with " + e.getMessage());
            OpenApiException ex = new OpenApiException(OpenApiServiceErrorEnum.SYSTEM_BUSY, throwable);
            throw ex;
        }
    }

}
