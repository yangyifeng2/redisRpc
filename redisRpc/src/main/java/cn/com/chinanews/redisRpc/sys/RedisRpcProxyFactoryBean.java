package cn.com.chinanews.redisRpc.sys;


import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * 动态代理bean
 * @author:yy
 */
public class RedisRpcProxyFactoryBean extends RedisRpcClientInterceptor implements FactoryBean<Object>{
	
	
	private Object serviceProxy;
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
	}


	@Override
	public Object getObject() {
		return this.serviceProxy;
	}

	@Override
	public Class<?> getObjectType() {
		return getServiceInterface();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
