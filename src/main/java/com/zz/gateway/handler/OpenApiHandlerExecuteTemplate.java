/**
 * 
 */
package com.zz.gateway.handler;

import org.apache.commons.chain.Context;

/**
 * @author Administrator
 *
 */
public interface OpenApiHandlerExecuteTemplate {

    /**
     * @param blCtx
     */
    boolean execute(Context chainContext) throws Exception ;

}
