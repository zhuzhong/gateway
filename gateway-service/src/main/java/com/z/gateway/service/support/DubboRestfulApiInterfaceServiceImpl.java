/**
 * 
 */
package com.z.gateway.service.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.z.gateway.common.entity.ApiInterface;
import com.z.gateway.service.ApiInterfaceService;

/**
 * 当后端的服务是dubbo提供的restful服务时接入
 * 
 * @author zhuzhong
 *
 */
public class DubboRestfulApiInterfaceServiceImpl implements ApiInterfaceService, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(DubboRestfulApiInterfaceServiceImpl.class);
	private String zkUrl;// 注册服务器的地址
	private DataSource dataSource;

	public void setZkUrl(String zkUrl) {
		this.zkUrl = zkUrl;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public ApiInterface findOne(String apiId, String version) {
		String contextPath = contextPaths.get(apiId);
		String targetUrl = restfulPaths.get(apiId);
		List<String> hosts = hostURLs.get(contextPath);
		String host = selectOne(targetUrl, hosts);
		ApiInterface ret = new ApiInterface();
		ret.setApiId(apiId);
		ret.setHostAddress(host);
		ret.setProtocol("http");
		ret.setRequestMethod("POST");
		ret.setTargetUrl(targetUrl);
		ret.setVersion(version);
		return ret;
	}

	private String selectOne(String targetPath, List<String> hosts) {
		Collections.shuffle(hosts);
		return hosts.get(0);
	}

	private ConcurrentHashMap<String/* contextpath */, List<String>/* host地址 */> hostURLs = new ConcurrentHashMap<>();

	private Map<String/* apiId */, String/* restful full path */> restfulPaths = new HashMap<>();
	private Map<String/* apiId */, String/* context path */> contextPaths = new HashMap<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("从db中获取数据，填充restfulPaths,contextPaths");
		logger.info("从注册中心获取数据填充hostURLs,而这个是动态变化的，这个可能时时更新");
	}

}
