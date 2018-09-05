package com.cns.redisRpc.sys;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 
 * @Description:TODO
 * @author:yy
 * @time:2018年8月29日 下午4:44:45
 */
public abstract class RedisRemoteAccessor extends RedisRemotingSupport {

	private Class<?> serviceInterface;
	private String serverName;
	private long readTimeOut;
	private boolean isUseCache;
	private long cacheTime;
	private RedisTemplate<String,Object> redisTemplate;
	
	

	public boolean isUseCache() {
		return isUseCache;
	}

	public void setUseCache(boolean isUseCache) {
		this.isUseCache = isUseCache;
	}

	public long getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}

	public void setServiceInterface(Class<?> serviceInterface) {
		if (serviceInterface != null && !serviceInterface.isInterface()) {
			throw new IllegalArgumentException("'serviceInterface' must be an interface");
		}
		this.serviceInterface = serviceInterface;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public long getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(long readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	/**
	 * Return the interface of the service to access.
	 */
	public Class<?> getServiceInterface() {
		return this.serviceInterface;
	}

}
