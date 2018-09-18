package cn.com.chinanews.redisRpc.sys;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.springframework.data.redis.core.RedisTemplate;

import cn.com.chinanews.redisRpc.boot.RedisRpcException;
import cn.com.chinanews.redisRpc.boot.RedisTopicMessage;
import cn.com.chinanews.redisRpc.boot.Result;
import cn.com.chinanews.redisRpc.boot.RpcContants;

/**
 * 客户端运行反射方法
 * @author:yy
 */
@SuppressWarnings("serial")
public class RedisRpcProxy  implements InvocationHandler, Serializable {

	private Class<?> _type;
	protected RedisRpcProxyFactory _factory;
	
	
	 protected RedisRpcProxy(RedisRpcProxyFactory factory, 
	                         Class<?> type){
	    _factory = factory;
	    _type = type;
	 }
	
	

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		
		String topicName=_type.getSimpleName();
		String rpcServiceName=_factory.getRpcServiceName();
		long readTimeOut=_factory.getReadTimeout();
		if(readTimeOut<=0l){
			readTimeOut=RpcContants.REDIS_READTIMEOUT;
		}
		if(rpcServiceName!=null){
			topicName=rpcServiceName;
		}else{
			topicName=toLowerCaseFirstOne(topicName);
		}
		
		long cacheTime=_factory.getCacheTime();
		if(cacheTime<=0l){
			cacheTime=RpcContants.REDISRPC_CACHEDTIME;
		}
		
		boolean isuserCache=_factory.isUseCache();
		
		RedisTemplate<String,Object> redisTemplate=RedisRPCSpringApplicationContextHolder.getRpcRedisTemplate();
		RedisTopicMessage rtm=new RedisTopicMessage(method.getName(),args,topicName,isuserCache,cacheTime);
		//先开始获取数据
		Result result=null;
		try{
			//多线程获取返回值
			result=ThreadPool.getServiceBack(rtm,readTimeOut);
			if(!result.isSuccess()){
				throw new RedisRpcException(result.getMessage());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result.getData();
	}
	

	public static String toLowerCaseFirstOne(String s){
	  if(Character.isLowerCase(s.charAt(0)))
	    return s;
	  else
	    return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	
}
