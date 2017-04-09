/**
 * 
 */
package com.zz.gateway.core;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.util.EntityUtils;

/**
 * @author sunff
 *
 */
public class HttpClientImpl implements ApiMultiHttpClientService {

	private static PoolingHttpClientConnectionManager manager = null;
	private static CloseableHttpClient httpClient = null;

	private static synchronized CloseableHttpClient getHttpClient() {
		if (httpClient == null) {
			// 注册访问协议相关的socket工厂
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https",
							SSLConnectionSocketFactory.getSystemSocketFactory())
					.build();
			// httpclient 工厂
			HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
					DefaultHttpRequestWriterFactory.INSTANCE,
					DefaultHttpResponseParserFactory.INSTANCE);
			// dns解析器
			DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
			// 创建池化连接管理器
			manager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry, connFactory, dnsResolver);
			// 默认socket配置
			SocketConfig defaultSocketConfig = SocketConfig.custom()
					.setTcpNoDelay(true).build();
			manager.setDefaultSocketConfig(defaultSocketConfig);

			manager.setMaxTotal(300);// 设置整个连接池的最大连接数
			manager.setDefaultMaxPerRoute(200);// 每个路由最大连接数
			manager.setValidateAfterInactivity(5 * 1000);

			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setConnectTimeout(2 * 1000) // 2s
					.setSocketTimeout(5 * 1000) // 5s
					.setConnectionRequestTimeout(2000).build();
			httpClient = HttpClients
					.custom()
					.setConnectionManager(manager)
					.setConnectionManagerShared(false)
					.evictIdleConnections(60, TimeUnit.SECONDS)
					.evictExpiredConnections()
					.setConnectionTimeToLive(60, TimeUnit.SECONDS)
					.setDefaultRequestConfig(defaultRequestConfig)
					.setConnectionReuseStrategy(
							DefaultConnectionReuseStrategy.INSTANCE)
					.setKeepAliveStrategy(
							DefaultConnectionKeepAliveStrategy.INSTANCE)
					.setRetryHandler(
							new DefaultHttpRequestRetryHandler(0, false))
					.build();

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						httpClient.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});

		}
		return httpClient;
	}

	@Override
	public String doHttpsPost(String url, String content, String contentType) {
		return null;
	}

	@Override
	public String doPost(String url, String reqData, String contentType) {
		String body = "";
		org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(
				url);
		httpPost.setHeader("Content-type", contentType);
		httpPost.setEntity(new StringEntity(reqData, "utf-8"));
		try {
			// 执行请求操作，并拿到结果（同步阻塞）
			CloseableHttpResponse response = getHttpClient().execute(httpPost);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			// 释放链接
			response.close();
		} catch (IOException e) {

		}
		return body;
	}

	@Override
	public String doPost(String url, String reqData, String contentType,
			String params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> HttpGet(String webUrl, Map paramMap) {

		return null;
	}

	@Override
	public Map<String, String> HttpGet(String url, String method, Map paramMap) {

		return null;
	}

	@Override
	public String HttpPost(String webUrl, Map paramMap) {
		return null;
	}

	@Override
	public String HttpPost(String url, String method, Map paramMap) {

		return null;
	}
}
