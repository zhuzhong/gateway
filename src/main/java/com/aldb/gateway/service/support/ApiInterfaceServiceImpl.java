/**
 * 
 */
package com.aldb.gateway.service.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.aldb.gateway.core.ApiInterface;
import com.aldb.gateway.service.ApiInterfaceService;
import com.aldb.gateway.util.CommonCodeConstants;

/**
 * @author Administrator
 *
 */
@Service
public class ApiInterfaceServiceImpl implements ApiInterfaceService {

    private static Log log = LogFactory.getLog(ApiInterfaceServiceImpl.class);

    @Override
    public ApiInterface findOne(String apiId, String version) {
        log.info("为了测试，关于这个接口，现在直接返回一个接口的值");
        if (apiId.equals("1")) {

            ApiInterface aif = new ApiInterface();
            aif.setApiId(apiId);
            aif.setVersion(version);
            aif.setProtocol("http");
            aif.setHostAddress("www.baidu.com");
            // aif.setPort(null);
            aif.setRequestMethod(CommonCodeConstants.REQUEST_METHOD.GET.name());
            // aif.setTargetUrl("/");
            return aif;
        } else if (apiId.equals("2")) {
            ApiInterface aif = new ApiInterface();
            aif.setApiId(apiId);
            aif.setVersion(version);
            aif.setProtocol("http");
            aif.setHostAddress("www.sina.com");
            // aif.setPort(null);
            aif.setRequestMethod(CommonCodeConstants.REQUEST_METHOD.GET.name());
            // aif.setTargetUrl("/");
            return aif;
        }else if (apiId.equals("3")) {
            ApiInterface aif = new ApiInterface();
            aif.setApiId(apiId);
            aif.setVersion(version);
            aif.setProtocol("http");
            aif.setHostAddress("www.jd.com");
            // aif.setPort(null);
            aif.setRequestMethod(CommonCodeConstants.REQUEST_METHOD.GET.name());
            // aif.setTargetUrl("/");
            return aif;
        }
        return null;

    }

}
