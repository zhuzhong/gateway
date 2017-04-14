/**
 * 
 */
package com.aldb.gateway.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aldb.gateway.handler.OpenApiAcceptHandler;

/**
 * @author Administrator
 *
 */
@Controller
public class GateWayController {

    
    
   @Autowired
    private OpenApiAcceptHandler acceptHandler;
    
    @RequestMapping(value = "service",method = {RequestMethod.POST,RequestMethod.GET})
    public void accessOpenApi(HttpServletRequest request, HttpServletResponse response) {
        this.acceptHandler.acceptRequest(request, response);
    }
}
