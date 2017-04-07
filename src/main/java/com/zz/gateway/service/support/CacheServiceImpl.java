/**
 * 
 */
package com.zz.gateway.service.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zz.gateway.service.CacheService;

/**
 * @author Administrator
 *
 */
@Service("cacheServiceImpl")
public class CacheServiceImpl implements CacheService {

    private Map<String, Object> m = new HashMap<String, Object>();

    @Override
    public void put(String key, Object obj) {
        m.put(key, obj);
    }

    @Override
    public Object get(String key) {
        return m.get(key);
    }

    @Override
    public void remove(String key) {
        m.remove(key);

    }

}
