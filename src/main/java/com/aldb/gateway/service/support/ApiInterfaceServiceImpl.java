/**
 * 
 */
package com.aldb.gateway.service.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.aldb.gateway.core.ApiInterface;
import com.aldb.gateway.service.ApiInterfaceService;

/**
 * @author Administrator
 *
 */
@Service
public class ApiInterfaceServiceImpl implements ApiInterfaceService{

   
    
    private static Log log=LogFactory.getLog(ApiInterfaceServiceImpl.class);
    @Override
    public ApiInterface findOne(String apiId, String version) {
        log.info("为了测试，关于这个接口，现在直接返回一个接口的值");
        ApiInterface aif=new ApiInterface();
        aif.setTargetUrl("http://www.baidu.com/");
        return aif;
    }
    
    
}
