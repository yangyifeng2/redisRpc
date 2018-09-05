package com.cns.redisRpc.sys;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

/**
 * 动态代理
 * @Description:TODO
 * @author:yy
 * @time:2018年8月29日 下午4:47:44
 */
public class RedisRpcProxyFactory  implements ObjectFactory {

	
	private final ClassLoader _loader;
	private long _readTimeout = -1;
	private String _rpcServiceName;
	private boolean isUseCache=false;
	private long cacheTime;
	
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

	public String getRpcServiceName() {
		return _rpcServiceName;
	}

	public void setRpcServiceName(String rpcServiceName) {
		this._rpcServiceName = rpcServiceName;
	}

	/**
	   * Creates the new proxy factory.
	   */
	  public RedisRpcProxyFactory()
	  {
	    this(Thread.currentThread().getContextClassLoader());
	  }

	  public long getReadTimeout() {
		return _readTimeout;
	}

	public void setReadTimeout(long _readTimeout) {
		this._readTimeout = _readTimeout;
	}

	/**
	   * Creates the new proxy factory.
	   */
	  public RedisRpcProxyFactory(ClassLoader loader)
	  {
	    _loader = loader;
	  }

	  
	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	

	 public Object create(Class<?> api, ClassLoader loader)
	  {
	    if (api == null)
	      throw new NullPointerException("api must not be null for RedisRpcProxyFactory.create()");
	    
	    InvocationHandler handler = new RedisRpcProxy(this,api);
	    return Proxy.newProxyInstance(loader,
                new Class[] { api},
                handler);
	    
	  }
	
	
}
