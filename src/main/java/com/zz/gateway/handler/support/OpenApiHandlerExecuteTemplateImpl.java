/**
 * 
 */
package com.zz.gateway.handler.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zz.gateway.handler.OpenApiHandlerExecuteTemplate;

/**
 * @author Administrator
 *
 */
public class OpenApiHandlerExecuteTemplateImpl implements OpenApiHandlerExecuteTemplate, Chain {

    private static Log logger = LogFactory.getLog(OpenApiHandlerExecuteTemplateImpl.class);
    private List<Command> commands = new ArrayList<Command>();

    public OpenApiHandlerExecuteTemplateImpl(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void addCommand(Command command) {
        this.commands.add(command);
    }

    @Override
    public boolean execute(Context context) throws Exception {
        logger.info("executing all handlers,have a good journey!");
        if (context == null || null == this.commands) {
            throw new IllegalArgumentException();
        }
        Iterator<Command> cmdIterator = commands.iterator();
        Command cmd = null;
        while (cmdIterator.hasNext()) {
            cmd = (Command) cmdIterator.next();
            cmd.execute(context);
        }
        return false;
    }

}
