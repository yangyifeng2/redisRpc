package com.chinanews.redisRpc.sys;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * redisRPC注解
 * @Description:TODO
 * @author:yy
 * @time:2018年8月29日 下午4:59:08
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface RedisRpcService{
	String value() default "";
}
