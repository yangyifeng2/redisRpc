package com.chinanews.redisRpc.sys;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * 属性文件获取类
 * @Description:TODO
 * @author:yy
 * @time:2018年8月31日 下午5:09:05
 */
@Component
public class PropertiesUtils implements EmbeddedValueResolverAware {
 
    private static StringValueResolver stringValueResolver;
 
    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        stringValueResolver = resolver;
    }
 
    public static String getPropertiesValue(String name){
        return stringValueResolver.resolveStringValue(name);
    }


}
