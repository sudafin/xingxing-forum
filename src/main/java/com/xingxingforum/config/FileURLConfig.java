package com.xingxingforum.config;

import com.qiniu.util.Auth;
import com.xingxingforum.config.properties.OssProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class FileURLConfig {
    @Resource
    private OssProperties ossProperties;
    public String uploadFile(String fileName) {
        String encodedFileName = null;
        encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        String publicUrl = String.format("%s/%s",ossProperties.getDomainOfBucket(), encodedFileName);
        Auth auth = Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
        long expireSeconds = 3600; //1小时
        return auth.privateDownloadUrl(publicUrl, expireSeconds);
    }
}
