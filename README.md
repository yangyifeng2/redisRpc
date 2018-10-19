# redisRpc
理论中最快的RPC，利用redis作为中间层做数据传导，所有的数据传递都保存在redis中，可以设置redis做为数据的中间层缓存，系统中的io压力完全转移到redis中。配置简单，有redisTemplate就可以直接使用，需要服务端和应用端使用同一redis。redis不需要特别的配置，支持spring，支持springBoot，相关代码参考了springRPC.

使用说明：
服务端：
必要条件：spring或springBoot
1.使用@RedisRpcService注解标志服务
示例：
@RedisRpcService("testService")
public class TestServiceImpl implements TestService, Serializable{

	@Override
	public String getTestName(String name) {
		// TODO Auto-generated method stub
		return "hello world! your name :"+name;
	}

}
