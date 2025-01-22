package com.xingxingforum.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "xf")  //这是一级前缀
//获取不需要验证的网址
public class AuthProperties {
    private String[] noAuthPaths; //二级前缀获得三级数据
}
