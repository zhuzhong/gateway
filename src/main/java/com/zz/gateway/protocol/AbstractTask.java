package com.zz.gateway.protocol;

import java.util.concurrent.Callable;

public abstract class AbstractTask implements Callable<Object>{

	public AbstractTask(){
	}
	
	@Override
	public Object call() throws Exception {
		Object obj = doBussiness();
		return obj;
	}

	
	public abstract Object doBussiness() throws Exception;

}
