package com.chinanews.redisRpc.boot;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisPoolConfig;


/**
 * 指定的redisTemplate
 * @Description:TODO
 * @author:yy
 * @time:2018年8月29日 下午4:49:08
 */
@Configuration
public class RpcConfig {

	@Bean(name = "redisRpcMqTemplate")
    public RedisTemplate<String, Object> redisRpcMqTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

	
	 
	//@Bean(name = "redisRpcMqTemplate2")
	public RedisTemplate<String, Object> redisTemplate(
			@Value("${redis123.hostName}") String hostName,
			@Value("${redis123.port}") int port,
			@Value("${redis123.password}") String password,
			@Value("${redis123.maxIdle}") int maxIdle,
			@Value("${redis123.maxTotal}") int maxTotal,
			@Value("${redis123.index}") int index,
			@Value("${redis123.maxWaitMillis}") long maxWaitMillis,
			@Value("${redis123.testOnBorrow}") boolean testOnBorrow) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(connectionFactory(hostName, port, password,
				maxIdle, maxTotal, index, maxWaitMillis, testOnBorrow));
		JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
		return template;
	}

	public RedisConnectionFactory connectionFactory(String hostName, int port,
			String password, int maxIdle, int maxTotal, int index,
			long maxWaitMillis, boolean testOnBorrow) {
		JedisConnectionFactory jedis = new JedisConnectionFactory();
		jedis.setHostName(hostName);
		jedis.setPort(port);
		if (!StringUtils.isEmpty(password)) {
			jedis.setPassword(password);
		}
		if (index != 0) {
			jedis.setDatabase(index);
		}
		jedis.setPoolConfig(poolCofig(maxIdle, maxTotal, maxWaitMillis,
				testOnBorrow));
		// 初始化连接pool
		jedis.afterPropertiesSet();
		RedisConnectionFactory factory = jedis;
 
		return factory;
	}
 
	public JedisPoolConfig poolCofig(int maxIdle, int maxTotal,
			long maxWaitMillis, boolean testOnBorrow) {
		JedisPoolConfig poolCofig = new JedisPoolConfig();
		poolCofig.setMaxIdle(maxIdle);
		poolCofig.setMaxTotal(maxTotal);
		poolCofig.setMaxWaitMillis(maxWaitMillis);
		poolCofig.setTestOnBorrow(testOnBorrow);
		return poolCofig;
	}
		
}
	
    

