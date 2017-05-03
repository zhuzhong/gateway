/**
 * 
 */
package com.aldb.gateway.core.support;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;

import com.aldb.gateway.common.util.CommonCodeConstants;
import com.aldb.gateway.core.OpenApiHttpClientService;

/**
 * @author sunff
 * 
 */
public class OpenApiHttpAsynClientServiceImpl implements OpenApiHttpClientService {

    public void init() {
        initHttpAsynClient();
    }

    public void close() {
        if (httpAsyncClient != null) {
            try {
                httpAsyncClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private CloseableHttpAsyncClient httpAsyncClient;

    private void initHttpAsynClient() {
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor();
        } catch (IOReactorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
        cm.setMaxTotal(100);
        httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(cm).build();
        httpAsyncClient.start();
    }

    private static class FutureCallbackImpl implements FutureCallback<HttpResponse> {

        private String body;

        public FutureCallbackImpl(String body) {
            this.body = body;
        }

        public void completed(final HttpResponse response) {
            // System.out.println(httpget.getRequestLine() + "->" +
            // response.getStatusLine())
            try {
                HttpEntity entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    if (entity != null) {
                        body = EntityUtils.toString(entity, "utf-8");
                    }
                    EntityUtils.consume(entity);
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        public void failed(final Exception ex) {

            // System.out.println(httpget.getRequestLine() + "->" + ex);
        }

        public void cancelled() {
            // System.out.println(httpget.getRequestLine() + " cancelled");
        }

    }

    @Override
    public String doGet(String webUrl, String traceId) {
        final HttpGet httpget = new HttpGet(webUrl);
        httpget.setHeader(CommonCodeConstants.TRACE_ID, traceId);

        String body = "";
        Future<HttpResponse> r = httpAsyncClient.execute(httpget, new FutureCallbackImpl(body));
        try {
            r.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return body;
    }

    @Override
    public String doGet(String webUrl, Map<String, String> paramMap, String traceId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String doHttpsGet(String webUrl, String traceId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String doHttpsGet(String webUrl, Map<String, String> paramMap, String traceId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String doHttpsPost(String url, String content, String contentType, String traceId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String doPost(String url, String reqData, String contentType, String traceId) {
        // TODO Auto-generated method stub
        return null;
    }
}
