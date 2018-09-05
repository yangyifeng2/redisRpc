package com.cns.redisRpc.boot;

import java.io.Serializable;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * 返回的结果封装类
 * @Description:TODO
 * @author:yy
 * @time:2018年8月29日 下午4:48:42
 */
public class Result  extends JdkSerializationRedisSerializer implements Serializable {

	private static final long serialVersionUID = -6301816888616649278L;
	/**
	 * 结果体
	 */
	protected Object data;
	/**
	 * 状态码
	 */
	protected Integer code;
	/**
	 * 信息
	 */
	protected String message;


	private Result() {
		super();
	}

	private Result(Integer code) {
		this.code = code;
	}

	public static Result create(Integer code) {
		return new Result(code);
	}

	public static Result createSuccessResult() {
		return create(ResultCode.SUCCESS);
	}

	public static Result createSuccessResult(Object data, String message) {
		return createSuccessResult().setData(data).setMessage(message);
	}

	public boolean isSuccess() {
		return code != null && code.equals(ResultCode.SUCCESS);
	}

	public Object getData() {
		return data;
	}

	public Result setData(Object data) {
		this.data = data;
		return this;
	}

	public Integer getCode() {
		return code;
	}

	public Result setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Result setMessage(String message) {
		this.message = message;
		return this;
	}

	
}
