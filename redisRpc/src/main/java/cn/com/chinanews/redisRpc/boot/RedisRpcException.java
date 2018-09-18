package cn.com.chinanews.redisRpc.boot;

/**
 * 自定义抛出异常
 * @author:yy
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
