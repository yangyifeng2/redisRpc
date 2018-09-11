package com.chinanews.redisRpc.sys;

import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @Description:TODO
 * @author:yy
 * @time:2018年8月29日 下午4:44:37
 */
public abstract class RedisBasedRemoteAccessor extends RedisRemoteAccessor implements InitializingBean {

	@Override
	public void afterPropertiesSet() {
		
	}

}

