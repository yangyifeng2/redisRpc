package com.chinanews.redisRpc.boot;


import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.chinanews.redisRpc.sys.RedisRPCSpringApplicationContextHolder;

/**
 * 消费者
 * @Description:TODO
 * @author:yy
 * @time:2018年9月4日 上午9:15:47
 */
@Component
public class Receiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
	
	
	public void runRMT(RedisTopicMessage rmt){
		
	}
	
    @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public void receiveMessage(Object message) {
    	
       LOGGER.info("Received <" + message+ ">");
       if(message instanceof RedisTopicMessage) {
        	
        }else{
        	LOGGER.error("The incoming data is not RedisTopicMessage, skipping.");
        	return;
        }
        
       RedisTopicMessage rmt=(RedisTopicMessage)message;
       
        RedisTemplate redisTemplate=RedisRPCSpringApplicationContextHolder.getRpcRedisTemplate();
        Result result=Result.createSuccessResult();
        
        
        
        String uuid="";
        if(rmt==null){
			result.setCode(ResultCode.DAO_ERROR);
			result.setMessage("Service method failed!");
        }else{
        	uuid=rmt.getUuid();
        	//先判断uid是否被占用或缓存数据已经存在了，用于判断是否已经有消费者在消费或数据已经被缓存中了
        	boolean islock=redisTemplate.opsForValue().setIfAbsent(uuid, "true");
        	if(islock){
        		redisTemplate.expire(uuid, 2000,TimeUnit.MILLISECONDS);
        	}else{
        		//如果其它消费者已经消费了，此处不再运行！
        		return ;
        	}
        	
        	String rpcServiceName=rmt.getRpcServiceName();
        	Object[] param=rmt.getArgs();
            String methodName=rmt.getMethodName();
            try {
    			
    			Object objbean=RedisRPCSpringApplicationContextHolder.getSpringBean(rpcServiceName);
    			Class<? extends Object> clazz=objbean.getClass();
    			
    			Object res=null;
    			
    			Method mm=null;
    			if(param.length>0){
        			Class[] clazzs=new Class[param.length];
        			for(int i=0;i<param.length;i++){
        				clazzs[i]=param[i].getClass();
        			}
        			mm=clazz.getMethod(methodName, clazzs);
    			}else{
    				mm=clazz.getMethod(methodName);
    			}
    			
    			try {
    				res=mm.invoke(objbean, param);
    				result.setData(res);
    				result.setMessage("success");
    			    				
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				result.setCode(ResultCode.DAO_ERROR);
    				result.setMessage("Method failed to run");
    			}
    			
    		
    		} catch (NoSuchMethodException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			result.setCode(ResultCode.METHOD_ERROR);
    			result.setMessage("没有找到相对应的方法");
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			result.setCode(ResultCode.SERVICE_ERROR);
    			result.setMessage("系统出现问题");
    			e.printStackTrace();
    		}
        }
        
        redisTemplate.opsForValue().set(RpcContants.getVId(uuid),result,rmt.getCacheTime(),TimeUnit.MILLISECONDS);
    }
}
