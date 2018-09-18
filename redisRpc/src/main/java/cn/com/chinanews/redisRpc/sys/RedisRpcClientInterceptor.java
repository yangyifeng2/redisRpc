package cn.com.chinanews.redisRpc.sys;

import java.net.MalformedURLException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.remoting.RemoteProxyFailureException;
import org.springframework.util.Assert;

/**
 * @author:yy
 */
public class RedisRpcClientInterceptor extends RedisBasedRemoteAccessor implements MethodInterceptor {


	
	private RedisRpcProxyFactory proxyFactory = new RedisRpcProxyFactory();
	private Object redisRpcProxy;

	/**
	 * Set the HessianProxyFactory instance to use. If not specified, a default
	 * HessianProxyFactory will be created.
	 * <p>
	 * Allows to use an externally configured factory instance, in particular a
	 * custom HessianProxyFactory subclass.
	 */
	public void setProxyFactory(RedisRpcProxyFactory proxyFactory) {
		this.proxyFactory = (proxyFactory != null ? proxyFactory : new RedisRpcProxyFactory());
	}

	/**
	 * Specify the Hessian SerializerFactory to use.
	 * <p>
	 * This will typically be passed in as an inner bean definition of type
	 * {@code com.caucho.hessian.io.SerializerFactory}, with custom bean
	 * property values applied.
	 */

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		prepare();
	}

	
	public void prepare() throws RemoteLookupFailureException {

		this.redisRpcProxy = createRpcProxy(this.proxyFactory);

	}

	
	protected Object createRpcProxy(RedisRpcProxyFactory proxyFactory) {
		return proxyFactory.create(getServiceInterface(), getBeanClassLoader());
	}

	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (this.redisRpcProxy == null) {
			throw new IllegalStateException("HessianClientInterceptor is not properly initialized - "
					+ "invoke 'prepare' before attempting any operations");
		}

		ClassLoader originalClassLoader = overrideThreadContextClassLoader();
		try {
			return invocation.getMethod().invoke(this.redisRpcProxy, invocation.getArguments());
		} catch (Throwable ex) {
			throw new RemoteProxyFailureException("", ex);
		} finally {
			resetThreadContextClassLoader(originalClassLoader);
		}
	}


	public void setRedisServiceName(String rpcServiceName){
		 this.proxyFactory.setRpcServiceName(rpcServiceName);
	}
	
	public void setReadTimeOut(long readTimeOut){
		 this.proxyFactory.setReadTimeout(readTimeOut);
	}
	
	public void setUseCache(boolean isUseCache) {
		this.proxyFactory.setUseCache(isUseCache);
	}
	
	public void setCacheTime(long cacheTime) {
		this.proxyFactory.setCacheTime(cacheTime);
	}
}
