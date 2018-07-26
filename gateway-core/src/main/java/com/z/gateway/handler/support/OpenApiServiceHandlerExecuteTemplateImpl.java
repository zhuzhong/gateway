/**
 * 
 */
package com.z.gateway.handler.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z.gateway.handler.OpenApiHandlerExecuteTemplate;

/**
 * @author Administrator
 *
 */
public class OpenApiServiceHandlerExecuteTemplateImpl implements OpenApiHandlerExecuteTemplate {

    private static Logger logger = LoggerFactory.getLogger(OpenApiServiceHandlerExecuteTemplateImpl.class);
    private List<Command> commands = new ArrayList<Command>();

    public OpenApiServiceHandlerExecuteTemplateImpl(List<Command> commands) {
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
            if (cmd.execute(context)) {
                break; //有一个处理它了，就直接跳出
            }
        }
        return false;
    }

}
