# redisRpc
理论中最快的RPC，jar包超小，项目侵入性低，与spring无缝衔接，使用简单，利用redis作为中间层做数据传导，所有的数据传递都保存在redis中，可以设置redis做为数据的中间层缓存，系统中的io压力完全转移到redis中。配置简单，有redisTemplate就可以直接使用，需要服务端和应用端使用同一redis。redis不需要特别的配置，支持spring，支持springBoot，相关代码参考了springRPC.

# maven
```Java
<dependency>
  <groupId>cn.com.chinanews</groupId>
  <artifactId>redisRpc</artifactId>
  <version>1.0.0</version>
</dependency>
```

# 必要条件：
## 1、spring或springBoot+redisTemplate已经搭建成功
## 2、需要maven导入fastjson
## 3、springBoot或spring需要设置扫描redisRPCcode目录。
```Java
@SpringBootApplication
@ComponentScan(basePackages={"cn.com.chinanews.redisRpc","自已的目录 "})
@Configuration
public class App{
}
```

## 4、定义相同的接口，比如TestService

## 5、简易使用：
服务端：
1.使用@RedisRpcService注解标志服务
示例：
```Java
@RedisRpcService("testService")
public class TestServiceImpl implements TestService, Serializable{
	@Override
	public String getTestName(String name) {
		// TODO Auto-generated method stub
		return "hello world! your name :"+name;
	}
}
```

客户端：
```Java
@Bean
public RedisRpcProxyFactoryBean redishelloClient() {
     RedisRpcProxyFactoryBean factory = new RedisRpcProxyFactoryBean();
     factory.setServiceInterface(TestService.class);
     return factory;
}
```
调用时，和spring的服务service一样的使用即可。


## 6、扩展使用：
1、客户端：设置开启rpc缓存后，相同参数的rpc调用将会在设置的缓存时间内在redis中被缓存下来，直到缓存失效为止，可最大效率利用性能，减少系统压力。
```Java
@Bean
public RedisRpcProxyFactoryBean redishelloClient() {
	RedisRpcProxyFactoryBean factory = new RedisRpcProxyFactoryBean();
	factory.setUseCache(true);//开启rpc缓存
	factory.setCacheTime(300000);//设置相关参数数据缓存的时间
	factory.setReadTimeOut(13000);//设置rpc连接的超时时间
	factory.setServiceInterface(TestService.class);//设置接口
	return factory;
}
```

2、设置多个redisTemplate:有些系统可能存在多个redisTemplate,可以通过下面的设置，设置redisRpc使用具体的redisTemplate
```Java
@Bean
public RedisRPCSpringApplicationContextHolder redishelloClient2() {
	RedisRPCSpringApplicationContextHolder factory = new RedisRPCSpringApplicationContextHolder();
	factory.setRedisTemplate(要设置的redisTemplate);
	return factory;
}
```

*　注：由于项目系个人开发，相关功能细节会有缺失，欢迎使用者多多指正，欢迎使用者提交代码共同成长。
