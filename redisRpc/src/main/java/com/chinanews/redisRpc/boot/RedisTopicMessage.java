package com.chinanews.redisRpc.boot;

import java.io.Serializable;
import java.util.UUID;

import com.alibaba.fastjson.JSON;

/**
 * 发送消息的封装类
 * @Description:TODO
 * @author:yy
 * @time:2018年8月29日 下午4:48:37
 */
public class RedisTopicMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String methodName;
	private Object[] args;
	private String uuid;
	private String rpcServiceName;
	private boolean isUserCache;
	private long cacheTime;
	
	
	public boolean isUserCache() {
		return isUserCache;
	}
	public long getCacheTime() {
		return cacheTime;
	}
	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}
	public String getRpcServiceName() {
		return rpcServiceName;
	}
	public void setRpcServiceName(String rpcServiceName) {
		this.rpcServiceName = rpcServiceName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getUuid() {
		return uuid;
	}

	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	
	public RedisTopicMessage(String methodName, Object[] args,String rpcServiceName,boolean isUserCache,long cacheTime) {
		super();
		this.methodName = methodName;
		this.args = args;
		this.isUserCache=isUserCache;
		this.rpcServiceName=rpcServiceName;
		this.cacheTime=cacheTime;
		if(isUserCache){
			this.uuid=getStrByObjs(rpcServiceName,methodName,args);
		}else{
			this.uuid=getUUID32();
		}
	}
	
	private static String getUUID32(){
	    String uuid = UUID.randomUUID().toString();
	    return uuid;
	}
	
	private static String getStrByObjs(String rpcServiceName,String methodName,Object[] args){
		Object obj=JSON.toJSON(args);
		MD5 md5 = new MD5();
		return md5.doDigest(rpcServiceName+methodName+JSON.toJSONString(obj).toString());
	}
	
}
