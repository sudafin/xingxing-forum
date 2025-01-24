package com.xingxingforum.config;

import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;

@Component
public class MailConfig {
    @Resource
    private JavaMailSenderImpl mailSender;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Value("${spring.mail.username}")
    private String from;
    public boolean mail(String email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //生成随机验证码
        String code = IdUtil.randomUUID().substring(0, 6);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        //设置一个html邮件信息
        helper.setText(
                "<!DOCTYPE html>" +
                        "<html lang='zh-CN'>" +
                        "<head>" +
                        "    <meta charset='UTF-8'>" +
                        "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "    <title>验证码通知</title>" +
                        "    <style>" +
                        "        body {" +
                        "            font-family: Arial, sans-serif;" +
                        "            background-color: #f4f4f4;" +
                        "            margin: 0;" +
                        "            padding: 0;" +
                        "        }" +
                        "        .container {" +
                        "            width: 100%;" +
                        "            margin: 50px auto;" +
                        "            background-color: #ffffff;" +
                        "            padding: 20px;" +
                        "            border-radius: 8px;" +
                        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                        "        }" +
                        "        .header {" +
                        "            text-align: center;" +
                        "            margin-bottom: 20px;" +
                        "        }" +
                        "        .header img {" +
                        "            max-width: 150px;" +
                        "        }" +
                        "        .message {" +
                        "            font-size: 16px;" +
                        "            line-height: 1.5;" +
                        "            color: #333333;" +
                        "        }" +
                        "        .code {" +
                        "            font-size: 24px;" +
                        "            font-weight: bold;" +
                        "            color: #007bff;" +
                        "            text-align: center;" +
                        "            margin: 20px 0;" +
                        "        }" +
                        "        .footer {" +
                        "            text-align: center;" +
                        "            font-size: 14px;" +
                        "            color: #999999;" +
                        "            margin-top: 20px;" +
                        "        }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='container'>" +
                        "        <div class='header'>" +
                        "            <h1>验证码通知</h1>" +
                        "        </div>" +
                        "        <div class='message'>" +
                        "            您好，<br>" +
                        "            感谢您使用我们的服务。<br>" +
                        "            您的验证码为：" +
                        "        </div>" +
                        "        <div class='code'>" +
                        "            " + code +
                        "        </div>" +
                        "        <div class='message'>" +
                        "            请在1分钟内使用此验证码完成操作。<br>" +
                        "            如果这不是您本人的操作，请忽略此邮件。" +
                        "        </div>" +
                        "        <div class='footer'>" +
                        "            版权所有 © 2023 您的公司名称" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>",
                true);
        //设置邮件主题名
        helper.setSubject("FlowerPotNet验证码");
        //发给谁-》邮箱地址
        helper.setTo(email);
        //谁发的-》发送人邮箱
        helper.setFrom(from);
        //将邮箱验证码以邮件地址为key存入redis,1分钟过期
        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(1));
        mailSender.send(mimeMessage);
        return true;
    }

}
