package com.aldb.gateway.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aldb.gateway.util.ApiHttpUtil;
import com.aldb.gateway.util.StringUtil;

public class ApiMutiHttpClientUtil implements ApiMultiHttpClientService {

	public static final String TIME_OUT_ERROR = "timeout";

	private static final int CONNNECT_TIMEOUT = 60 * 1000;

	/**
	 * @see IdleConnectionTimeoutThread#setTimeoutInterval(long)
	 */
	private static final long CONNECTION_TIMEOUT_INTERVAL = 30 * 1000;

	private static IdleConnectionTimeoutThread idleConnectionTimeoutThread;

	public synchronized static void addConnectionManager(
			HttpConnectionManager connectionManager) {

		if (idleConnectionTimeoutThread == null) {
			idleConnectionTimeoutThread = new IdleConnectionTimeoutThread();
			idleConnectionTimeoutThread
					.setTimeoutInterval(CONNECTION_TIMEOUT_INTERVAL);
			idleConnectionTimeoutThread.setConnectionTimeout(CONNNECT_TIMEOUT);
			idleConnectionTimeoutThread.start();
		}
		idleConnectionTimeoutThread.addConnectionManager(connectionManager);
	}

	/**
	 * @since 3.1
	 */
	public synchronized static void removeConnectionManager(
			HttpConnectionManager connectionManager) {
		if (idleConnectionTimeoutThread == null) {
			return;
		}
		idleConnectionTimeoutThread.removeConnectionManager(connectionManager);
	}

	private HttpClient client = null;

	private static Log log = LogFactory.getLog(ApiMutiHttpClientUtil.class);

	public HttpClient getClient() {
		log.info("这个实例化后面需要处理，现在先这样...");
		if (client == null) {
			client = new HttpClient();
		}
		return client;
	}

	public void setClient(HttpClient client) {

		if (client != null) {
			HttpConnectionManager connectionManager = client
					.getHttpConnectionManager();

			if (connectionManager != null) {

				addConnectionManager(connectionManager);
			}
			this.client = client;
		}

	}

	@SuppressWarnings("deprecation")
	public String doPost(String url, String reqData, String contentType) {
		String response = null;
		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(0, false));
		getClient().setParams(httpClientParams);
		PostMethod method = new PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		// 设置Http Post数据
		try {
			method.setRequestBody(reqData);
			method.setRequestHeader("Content-type", contentType);
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				InputStream responseStream = method.getResponseBodyAsStream();
				response = convertStreamToString(responseStream);
			} else {
				response = TIME_OUT_ERROR;
			}
		} catch (IOException e) {
			e.printStackTrace();
			response = TIME_OUT_ERROR;
		} finally {
			method.releaseConnection();
		}
		return response;
	}

	@SuppressWarnings("deprecation")
	public String doPost(String url, String reqData, String contentType,
			String params) {
		String response = null;
		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(0, false));
		client.setParams(httpClientParams);
		PostMethod method = new PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		// 设置Http Post数据
		try {
			if (StringUtil.isNotEmpty(reqData)) {
				method.setRequestBody(reqData);
			} else {
				method.setRequestBody(params);
			}
			method.setRequestHeader("Content-type", contentType);
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				InputStream responseStream = method.getResponseBodyAsStream();
				response = convertStreamToString(responseStream);
			} else {
				response = TIME_OUT_ERROR;
			}
		} catch (IOException e) {
			e.printStackTrace();
			response = TIME_OUT_ERROR;
		} finally {
			method.releaseConnection();
		}
		return response;
	}

	/**
	 * 
	 * 方法描述...post方式请求服务器(https协议)
	 *
	 * @Title: doHttpsPost
	 * @date 2016年5月19日 下午3:24:28
	 * @author 丁耀东
	 * @VERSION v2.1
	 * @param url
	 *            请求地址
	 * @param content
	 *            请求参数
	 * @param contentType
	 *            请求参数格式
	 * @return response
	 */
	public String doHttpsPost(String url, String content, String contentType) {
		String response = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(new KeyManager[0],
					new TrustManager[] { new TrustAnyTrustManager() },
					new SecureRandom());
			URL console = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) console
					.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type", contentType);
			conn.connect();
			OutputStream out = conn.getOutputStream();
			out.write(content.getBytes("UTF-8"));
			// 刷新、关闭
			out.flush();
			out.close();
			if (conn.getResponseCode() == HttpStatus.SC_OK) {
				InputStream im = conn.getInputStream();
				response = convertStreamToString(im);
			} else {
				response = TIME_OUT_ERROR;
			}
		} catch (IOException e) {
			e.printStackTrace();
			response = TIME_OUT_ERROR;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			response = TIME_OUT_ERROR;
		} catch (KeyManagementException e) {
			e.printStackTrace();
			response = TIME_OUT_ERROR;
		}
		return response;
	}

	/**
	 * 
	 * 类描述...实现X509TrustManager，管理证书
	 *
	 * @ClassName: TrustAnyTrustManager
	 * @author 丁耀东
	 * @date 2016年5月19日 上午10:36:13
	 * @version V2.1
	 */
	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	/**
	 * 
	 * 类描述...保证IP地址也可以使用
	 *
	 * @ClassName: TrustAnyHostnameVerifier
	 * @author 丁耀东
	 * @date 2016年5月19日 下午3:19:01
	 * @version V2.1
	 */
	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	/**
	 * 将输入流转换为String
	 *
	 * 这个转过来乱码，需要处理一下
	 */
	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	public String HttpPost(String url, String method, Map paramMap) {
		return HttpPost(url + '/' + method, paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ald.gateway.core.ApiMultiHttpClientService#HttpPost(java.lang.String,
	 * java.util.Map)
	 */

	@SuppressWarnings("rawtypes")
	public String HttpPost(String webUrl, Map paramMap) {
		String encoding = "UTF-8";
		if (encoding == null || "".equals(encoding))
			encoding = "UTF-8";
		StringBuffer sBuffer = new StringBuffer();
		// httpClient.set
		// 创建POS方法的实例
		NameValuePair[] pairs = null;
		PostMethod postMethod = new PostMethod(webUrl);
		postMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		if (paramMap != null) {
			pairs = new NameValuePair[paramMap.size()];
			Set set = paramMap.keySet();
			Iterator it = set.iterator();
			int i = 0;
			while (it.hasNext()) {
				Object key = it.next();
				Object value = paramMap.get(key);
				if (!ApiHttpUtil.checkNull(value)) {
					pairs[i] = new NameValuePair(key.toString(),
							value.toString());
				}
				i++;
			}
			postMethod.setRequestBody(pairs);
		}
		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(0, false));
		client.setParams(httpClientParams);
		try {
			// 执行getMethod
			int statusCode = client.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ postMethod.getStatusLine());
				sBuffer = new StringBuffer();
			} else {
				sBuffer = new StringBuffer(postMethod.getResponseBodyAsString()
						+ "");
			}
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			postMethod.releaseConnection();
		}
		return sBuffer.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> HttpGet(String webUrl, Map paramMap) {

		// 设置编码格式
		String encoding = "UTF-8";
		if (encoding == null || "".equals(encoding))
			encoding = "UTF-8";
		String queryString = createLinkString(paramMap);
		webUrl = webUrl + "?" + queryString;
		StringBuffer sBuffer = new StringBuffer();
		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(0, false));
		client.setParams(httpClientParams);
		GetMethod gettMethod = null;
		int statusCode = -1;
		// httpClient.set
		try {
			URI uri = new URI(webUrl, false, encoding);

			gettMethod = new GetMethod(uri.toString());

			gettMethod.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);

			// 执行getMethod
			statusCode = client.executeMethod(gettMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ gettMethod.getStatusLine());
				sBuffer = new StringBuffer();
			} else {
				sBuffer = new StringBuffer(gettMethod.getResponseBodyAsString()
						+ "");
			}
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			// Rewriter
			// Rewrite Date：2015-12-22 By ZouYongle Case gettMethod MayBe is
			// NULL Start1：
			// gettMethod.releaseConnection();
			// End1：
			// Added by
			// Add date：2015-12-22 Start2：
			if (gettMethod != null) {
				gettMethod.releaseConnection();
			}
			// End2：
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("statusCode", String.valueOf(statusCode));
		map.put("rsp", sBuffer.toString());
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> HttpGet(String url, String method, Map paramMap) {
		String webUrl = url + "/" + method;
		return HttpGet(webUrl, paramMap);
	}

	/**
	 * 发送Get请求工具方法,处理参数有中文字符
	 * 
	 * @Methods Name HttpGet
	 * @Create In Dec 30, 2014 By songw
	 * @param url
	 * @param method
	 * @param paramMap
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String HttpGetByUtf(String url, String method, Map paramMap) {
		// 设置编码格式
		String encoding = "UTF-8";
		String webUrl = url + "/" + method;
		if (encoding == null || "".equals(encoding))
			encoding = "UTF-8";
		String queryString = createLinkString(paramMap);
		webUrl = webUrl + "?" + queryString;
		StringBuffer sBuffer = new StringBuffer();
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		GetMethod gettMethod = null;
		// httpClient.set
		try {
			URI uri = new URI(webUrl, false, encoding);

			gettMethod = new GetMethod(uri.toString());
			gettMethod.setRequestHeader("Content-type", "application/json");
			gettMethod.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(5000); // 连接5秒超时
			httpClient.getHttpConnectionManager().getParams()
					.setSoTimeout(30000);// 读取30秒超时

			// 执行getMethod
			int statusCode = httpClient.executeMethod(gettMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ gettMethod.getStatusLine());
				sBuffer = new StringBuffer();
			} else {
				sBuffer = new StringBuffer(gettMethod.getResponseBodyAsString()
						+ "");
			}
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			// Rewriter
			// Rewrite Date：2015-12-22 By ZouYongle Case gettMethod MayBe is
			// NULL Start1：
			// gettMethod.releaseConnection();
			// End1：
			// Added by
			// Add date：2015-12-22 Start2：
			if (gettMethod != null) {
				gettMethod.releaseConnection();
			}
			// End2：
		}
		return sBuffer.toString();
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

}
