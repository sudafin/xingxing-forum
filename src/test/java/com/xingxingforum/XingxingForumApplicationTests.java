package com.xingxingforum;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.qiniu.util.Auth;
import com.xingxingforum.config.FileURLConfig;
import com.xingxingforum.config.properties.OssProperties;
import com.xingxingforum.entity.model.UserForumRelations;
import com.xingxingforum.enums.RelationEnum;
import com.xingxingforum.mapper.UserForumRelationsMapper;
import com.xingxingforum.utils.RSAUtils;
import com.xingxingforum.utils.SearchIPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.tool.CodeGenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.KeyPair;
import java.time.Duration;

import static com.xingxingforum.constants.Constant.JWT_REFRESH_TTL;

@SpringBootTest
@Slf4j
class XingxingForumApplicationTests {
    @Resource
    private JavaMailSenderImpl mailSender;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private OssProperties ossProperties;

    @Resource
    private UserForumRelationsMapper userForumRelationsMapper;

    @Resource
    private FileURLConfig fileURLConfig;

    @Test
    void contextLoads() {
        KeyPair keyPair = RSAUtils.generateKeyPair();
        JWTSigner jwtSigner = JWTSignerUtil.hs256(RSAUtils.getPrivateKey(keyPair).getBytes());
        System.out.println(jwtSigner.getAlgorithm());
    }

    @Test
    void mail() throws MessagingException {

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
        helper.setTo("3535929427@qq.com");
        //谁发的-》发送人邮箱
        helper.setFrom(from);
        //将邮箱验证码以邮件地址为key存入redis,1分钟过期
        redisTemplate.opsForValue().set("email", code, Duration.ofMinutes(1));
        mailSender.send(mimeMessage);

    }

    @Test
    void testIp() throws IOException {
        System.out.println(SearchIPUtils.getIP("220.181.108.157"));
    }

    @Test
    void test_oss(){
        Auth auth = Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
        String token = auth.uploadToken(ossProperties.getBucket());
        System.out.println(token);
    }

    @Test
    void test_user_forum_relations(){
        UserForumRelations userForumRelations = new UserForumRelations();
        userForumRelations.setRelationType(RelationEnum.NORMAL);
        userForumRelations.setUserId(1L);
        userForumRelations.setForumId(1L);
        userForumRelationsMapper.insert(userForumRelations);
    }

    @Test
    void test_avatar_url(){
        String avatar = fileURLConfig.uploadFile("FjD4IK_gdSW_5LSh03scRBdxwFgC");
        log.debug("avatar:{}",avatar);
    }

    public static void main(String[] args) {
        Duration ttl = JWT_REFRESH_TTL;
        System.out.println(ttl.toMillis());
    }

}
