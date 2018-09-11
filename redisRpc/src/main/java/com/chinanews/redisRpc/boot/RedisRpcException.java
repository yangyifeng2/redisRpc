package com.chinanews.redisRpc.boot;

/**
 * 自定义抛出异常
 * @Description:TODO
 * @author:yy
 * @time:2018年8月29日 下午4:48:14
 */
public class RedisRpcException  extends RuntimeException {
	  /**
	   * Zero-arg constructor.
	   */
	  public RedisRpcException()
	  {
	  }

	  /**
	   * Create the exception.
	   */
	  public RedisRpcException(String message)
	  {
	    super(message);
	  }

	  /**
	   * Create the exception.
	   */
	  public RedisRpcException(String message, Throwable rootCause)
	  {
	    super(message, rootCause);
	  }

	  /**
	   * Create the exception.
	   */
	  public RedisRpcException(Throwable rootCause)
	  {
	    super(rootCause);
	  }
	}
