package com.zz.gateway.util;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zz.gateway.protocol.OpenApiHttpRequestBean;

public class OpenApiResponseUtils {


    
    private static Log logger = LogFactory.getLog(OpenApiResponseUtils.class);
    
    
    public static final String CONTENT_TYPE_KEY = "content-type";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String HEADER_HOST_KEY = "host";
    public static final String HEADER_SERVER_KEY = "server";
    public static Map<String, HttpServletResponse> sessionMap = new HashMap<String, HttpServletResponse>();

    public static void writeRsp(HttpServletResponse response, OpenApiHttpRequestBean requestBean) {
        setResponseHeader(response, requestBean.getReqHeader());
        try {
            PrintWriter writer = response.getWriter();
            writer.print(requestBean.getPrintStr());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            logger.error("Write body to response error, " + e.getMessage());
        } finally {
            sessionMap.remove(requestBean.getReqId());
        }
    }

    private static void setResponseHeader(HttpServletResponse response, Map<String, String> httpHeader) {
        Iterator<Entry<String, String>> entries = httpHeader.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, String> entry = entries.next();
            if (entry.getKey().equals(CONTENT_TYPE_KEY) || entry.getKey().equals(HEADER_HOST_KEY)) {
                response.addHeader(entry.getKey(), entry.getValue());
            }
        }
        response.setHeader(HEADER_SERVER_KEY, null);
    }
}
