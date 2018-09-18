package cn.com.chinanews.redisRpc.sys;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author:yy
 */
public abstract class RedisBasedRemoteAccessor extends RedisRemoteAccessor implements InitializingBean {

	@Override
	public void afterPropertiesSet() {
		
	}

}

