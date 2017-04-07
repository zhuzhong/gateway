/**
 * 
 */
package com.zz.gateway.service;

import com.zz.gateway.core.ApiInterface;

/**
 * @author Administrator
 *
 */
public interface ApiInterfaceService {

    /**
     * 根据apiId及版本号，获取一个后端服务api接口服务的信息
     * @param apiId
     * @param version
     * @return
     */
    ApiInterface findOne(String apiId,String version);
    
    
}
