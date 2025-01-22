package com.xingxingforum.config;

import com.xingxingforum.config.properties.TTLProperties;
import com.xingxingforum.utils.TTLGenerator;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
//配置工具类加载ttProperties
public class TTLConfiguration {

    private final TTLProperties ttlProperties;

    public TTLConfiguration(TTLProperties ttlProperties) {
        this.ttlProperties = ttlProperties;
    }

    //PostConstruct注解，在bean初始化后执行
    @PostConstruct
    public void init() {
        TTLGenerator.initProperties(ttlProperties);
    }
}