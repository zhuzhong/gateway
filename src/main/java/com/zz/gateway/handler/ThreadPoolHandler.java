/**
 * 
 */
package com.zz.gateway.handler;

import com.zz.gateway.protocol.AbstractTask;

/**
 * @author Administrator
 *
 */
public interface ThreadPoolHandler {

    
    public Object addTask(AbstractTask task);
}
