package com.chinanews.redisRpc.boot;


import java.util.concurrent.Callable;
import org.springframework.data.redis.core.RedisTemplate;
import com.chinanews.redisRpc.sys.RedisRPCSpringApplicationContextHolder;

/**
 * 异步发送信息给队列
 * @Description:TODO
 * @author:yy
 * @time:2018年8月29日 下午4:48:27
 */
public class RedisRpcGet implements Callable{

	private Result result;
	private long readTimeOut;
	private RedisTopicMessage rtm;
	
	@Override
	public Result call() throws RedisRpcException {
		// TODO Auto-generated method stub
		RedisTemplate<String,Object> redisTemplate=RedisRPCSpringApplicationContextHolder.getRpcRedisTemplate();
		long curReadTimeOut=System.currentTimeMillis();
		String serviceRpcKey=RpcContants.getVId(rtm.getUuid());
		
		
		boolean isqueue=false;
		result=null;
		//先从缓存里获取一次，如果存在就直接返回值，如果不存在，则发送topic
		if(rtm.isUserCache()){
			result=(Result) redisTemplate.opsForValue().get(serviceRpcKey);
			if(result==null){
				if(isqueue){
					redisTemplate.opsForList().leftPush(rtm.getRpcServiceName(),rtm);
				}else{
					redisTemplate.convertAndSend(rtm.getRpcServiceName(),rtm);
				}
				
			}
		}
		//异步获取相关信息
		while(result==null){
			result=(Result) redisTemplate.opsForValue().get(serviceRpcKey);
			if(System.currentTimeMillis()-curReadTimeOut>readTimeOut){
				throw new RedisRpcException(" Read the redisRpc is timeout.There may be no registration service.");
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}


	public RedisRpcGet(RedisTopicMessage rtm,long readTimeOut){
		super();
		this.rtm=rtm;
		this.readTimeOut=readTimeOut;
	}
	
}
