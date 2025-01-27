package com.xingxingforum.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 配置类信息
 **/
@Data
@ConfigurationProperties(prefix = "xf.swagger")
public class SwaggerConfigProperties implements Serializable {

    private Boolean enable;
    private Boolean enableResponseWrap;

    public String packagePath;

    public String title;

    public String description;

    public String contactName;

    public String contactUrl;

    public String contactEmail;

    public String version;
}
