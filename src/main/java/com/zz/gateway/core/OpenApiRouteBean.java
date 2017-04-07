/**
 * 
 */
package com.zz.gateway.core;

import java.util.Map;

/**
 * @author Administrator
 *
 */
public class OpenApiRouteBean {

    private String apiId;

    private String timeStamp;

    private Map<String, String> reqHeader;

    private String reqId; // 内部定义的请求id

    private String operationType;

    private String serviceReqData;

    private String version;

  

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Map<String, String> getReqHeader() {
        return reqHeader;
    }

    public void setReqHeader(Map<String, String> reqHeader) {
        this.reqHeader = reqHeader;
    }

}
