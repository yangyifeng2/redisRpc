package cn.com.chinanews.redisRpc.sys;

import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author:yy
 */
@Component
public class RedisRPCSpringApplicationContextHolder  implements ApplicationContextAware{  

	private static RedisTemplate<String, Object> redisTemplate;
	private static ApplicationContext context;  
	
    public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}


	@Override  
    public void setApplicationContext(ApplicationContext context) throws BeansException {  
    	RedisRPCSpringApplicationContextHolder.context = context;  
    }  
  
     
    public static Object getSpringBean(String beanName) {    
        return context==null?null:context.getBean(beanName);  
    }  
  
    public static String[] getBeanDefinitionNames() {  
        return context.getBeanDefinitionNames();  
    }  
    
    public static Map<String, Object> getAllServices(){
    	return  context.getBeansWithAnnotation(org.springframework.stereotype.Service.class);
    }


	 /**
	     * 注册bean
	     * @param beanId 所注册bean的id
	     * @param className bean的className，
	     *                     三种获取方式：1、直接书写，如：com.mvc.entity.User
	     *                                   2、User.class.getName
	     *                                   3.user.getClass().getName()
	     */
	 @SuppressWarnings("rawtypes")
	public static void registerBean(String beanId,String className,Map propertyMap) {
	        // get the BeanDefinitionBuilder
	    	
	    	ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) context;
	        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableContext.getBeanFactory();
	    	
	        BeanDefinitionBuilder beanDefinitionBuilder =BeanDefinitionBuilder.genericBeanDefinition(className);

	        // register the bean
	        if(propertyMap!=null){
	        	Iterator<?> entries = propertyMap.entrySet().iterator();
	        	Map.Entry<?, ?> entry;
	        	while (entries.hasNext()) {
	        	entry = (Map.Entry<?, ?>) entries.next();
	        	String key = (String) entry.getKey();
	        	Object val = entry.getValue();
	        	beanDefinitionBuilder.addPropertyValue(key, val);
	        }
	        
	        BeanDefinition beanDefinition=beanDefinitionBuilder.getBeanDefinition();
	       	beanDefinitionRegistry.registerBeanDefinition(beanId,beanDefinition);
	    }
	 }
	 
	 public static RedisTemplate<String,Object> getRpcRedisTemplate(){
		 if(redisTemplate!=null){
			 return redisTemplate;
		 }
		 return (RedisTemplate<String, Object>) getSpringBean("redisRpcMqTemplate");
	 }
	 
}
