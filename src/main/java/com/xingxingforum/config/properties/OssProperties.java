package com.xingxingforum.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "qiniu")  //这是一级前缀
public class OssProperties {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domainOfBucket;
}
