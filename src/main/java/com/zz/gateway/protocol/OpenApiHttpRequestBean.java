/**
 * 
 */
package com.zz.gateway.protocol;

import java.util.HashMap;
import java.util.Map;

import com.zz.gateway.util.CommonCodeConstants;

/**
 * 
 * 
 * @author Administrator
 *
 */
public class OpenApiHttpRequestBean {

	private Map<String, String> reqHeader;
	
	private String operationType;

	private String clientAddr; // 客户端ip
	private String localAddr;// 服务端ip
	private int localPort;// 服务端口
	private String queryString;
	private Map<String, String> thdApiUrlParams;// 第三方接口所需传入的url参数

	private String reqId; // 内部定义的请求id

	private String printStr; // 响应，这个最终写入response的流中
	
	private String serviceReqData;  //post请求方法参数
	private String requestMethod;
	private Map<String,Object> serviceGetReqData; //get请求参数
	
	

	public Map<String, Object> getServiceGetReqData() {
		return serviceGetReqData;
	}

	public void addServiceGetReqData(String key, Object value) {
		if(this.serviceGetReqData==null){
			this.serviceGetReqData=new HashMap<String,Object>();
		}
		this.serviceGetReqData.put(key, value);
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getPrintStr() {
		return printStr;
	}

	public void setPrintStr(String printStr) {
		this.printStr = printStr;
	}

	public String getServiceReqData() {
		return serviceReqData;
	}

	public void setServiceReqData(String serviceReqData) {
		this.serviceReqData = serviceReqData;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getClientAddr() {
		return clientAddr;
	}

	public void setClientAddr(String clientAddr) {
		this.clientAddr = clientAddr;
	}

	public String getLocalAddr() {
		return localAddr;
	}

	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public Map<String, String> getThdApiUrlParams() {
		return thdApiUrlParams;
	}

	public void setThdApiUrlParams(Map<String, String> thdApiUrlParams) {
		this.thdApiUrlParams = thdApiUrlParams;
	}

	public Map<String, String> getReqHeader() {
		return reqHeader;
	}

	public void setReqHeader(Map<String, String> reqHeader) {
		this.reqHeader = reqHeader;
	}

	// 业务请求参数
	private String request_data;

	public String getAppToken() {
		return appToken;
	}

	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getRequest_data() {
		return request_data;
	}

	public void setRequest_data(String request_data) {
		this.request_data = request_data;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getRedisKey() {
		if (this.operationType.equals(CommonCodeConstants.API_SERVICE_KEY)) {
			return CommonCodeConstants.getRouteBeanRedisKey(reqId);
		}

		return CommonCodeConstants.getRouteBeanRedisKey("");
	}

	private String serviceRsp; // 后端服务返回值

	public String getServiceRsp() {
		return serviceRsp;
	}

	public void setServiceRsp(String serviceRsp) {
		this.serviceRsp = serviceRsp;
	}

	// 公共的参数 begin ---
	private String appId;
	private String apiId;
	private String version;
	private String appToken;
	private String timeStamp;
	private String signMethod;
	private String sign;
	private String deviceToken;
	private String userToken;

	// 公共参数end----

	// 公共部分增加参数
	private String format;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSignMethod() {
		return signMethod;
	}

	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

}
