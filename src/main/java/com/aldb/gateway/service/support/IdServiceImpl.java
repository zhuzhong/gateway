/**
 * 
 */
package com.aldb.gateway.service.support;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.aldb.gateway.service.IdService;

/**
 * @author Administrator
 *
 */
@Service
public class IdServiceImpl implements IdService {

    private AtomicInteger ai = new AtomicInteger(1);

    @Override
    public String genInnerRequestId() {
        return String.valueOf(ai.getAndIncrement());
    }
    
    

}
