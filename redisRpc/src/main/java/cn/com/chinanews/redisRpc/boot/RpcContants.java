package cn.com.chinanews.redisRpc.boot;

/**
 * 常量类
 * @author:yy
 */
public class RpcContants {
	
	public static String REDISRPC_POOL="#_redisrpc_pool";
	public static String REDISRPC_SERVICE_POOL="service_key";
	
	public static String REDISRPC_MQ_PIX="redisRpc-mq-";
	public static String REDISRPC_V_PIX="redisRpc-v-";
	
	public static boolean REDISRPC_ISCACHED=false;
	public static long REDISRPC_CACHEDTIME=1000l;
	
	public static long REDIS_READTIMEOUT=3000l;
	
	public static String getMQId(String uuid){
		return REDISRPC_MQ_PIX+uuid;
	}
	
	public static String getVId(String uuid){
		return REDISRPC_V_PIX+uuid;
	}
}
