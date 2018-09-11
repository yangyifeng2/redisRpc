package com.chinanews.redisRpc.sys;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Service;

import com.chinanews.redisRpc.boot.Receiver;
import com.chinanews.redisRpc.boot.RedisTopicMessage;
import com.chinanews.redisRpc.boot.RpcContants;

/**
 * 服务端初始化类
 * @Description:TODO
 * @author:yy
 * @time:2018年9月4日 上午9:19:10
 */
@Service
public class RedisRpcInit implements ApplicationListener<ContextRefreshedEvent> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init() {

		Set<Topic> topicSet = new HashSet<Topic>();

		Annotation anno2;
		Class<? extends Object> clazz = null;
		Map<String, Object> services = RedisRPCSpringApplicationContextHolder.getAllServices();
		for (Entry<String, Object> it : services.entrySet()) {
			// 开始操作注解
			clazz = it.getValue().getClass();
			if (clazz.isAnnotationPresent(RedisRpcService.class)) {
				anno2 = clazz.getAnnotation(RedisRpcService.class);
				String topicName = ((RedisRpcService) anno2).value();
				topicSet.add(new PatternTopic(topicName));
			}
		}

		boolean isqueue=false;
		
		if (topicSet.size() > 0) {
			
			// 做一些初始化的事情,注册服务
			RedisTemplate<String, Object> redisTemplate =RedisRPCSpringApplicationContextHolder.getRpcRedisTemplate();
			if(isqueue){
				// 先注册服务
				for (Topic topic : topicSet) {
					String topicName = topic.getTopic();
					System.out.println("RedisRpcService :'"+topicName+"' is  registed." );
					while(true){
						//使用阻塞队列
						Object message=redisTemplate.opsForList().rightPop(topicName, 0, TimeUnit.SECONDS);
						Receiver receiver =new Receiver();
						receiver.receiveMessage(message);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else{
				// 先注册服务
				for (Topic topic : topicSet) {
					String topicName = topic.getTopic();
					//redisTemplate.opsForHash().put(RpcContants.REDISRPC_POOL, topicName, topicName);
					System.out.println("RedisRpcService :'"+topicName+"' is  registed." );
				}

				// 定义一个消费者监听器
				Receiver receiver = (Receiver) RedisRPCSpringApplicationContextHolder.getSpringBean("receiver");
				Map map = new HashMap();
				JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
				map.put("serializer", serializer);
				map.put("delegate", receiver);
				map.put("defaultListenerMethod", "receiveMessage");
				RedisRPCSpringApplicationContextHolder.registerBean("listenerAdapter", MessageListenerAdapter.class.getName(), map);

				MessageListenerAdapter listenerAdapter = (MessageListenerAdapter) RedisRPCSpringApplicationContextHolder
						.getSpringBean("listenerAdapter");
				Map map2 = new HashMap();
				map2.put("connectionFactory", redisTemplate.getConnectionFactory());
				RedisMessageListenerContainer container = new RedisMessageListenerContainer();
				container.setConnectionFactory(redisTemplate.getConnectionFactory());

				Map<MessageListener, Set<Topic>> listenerTopics = new ConcurrentHashMap<MessageListener, Set<Topic>>();

				listenerTopics.put(listenerAdapter, topicSet);

				map2.put("messageListeners", listenerTopics);
				RedisRPCSpringApplicationContextHolder.registerBean("container", RedisMessageListenerContainer.class.getName(),
						map2);
				// 再加入消费者
				RedisMessageListenerContainer c = (RedisMessageListenerContainer) RedisRPCSpringApplicationContextHolder
						.getSpringBean("container");
				c.start();
			}
			

			
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		// TODO Auto-generated method stub
		init();
	}

}
