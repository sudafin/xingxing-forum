package com.xingxingforum.controller;

import com.qiniu.util.Auth;
import com.xingxingforum.config.MailConfig;
import com.xingxingforum.config.properties.OssProperties;
import com.xingxingforum.entity.dto.users.InfoDTO;
import com.xingxingforum.entity.dto.users.LoginFormDTO;
import com.xingxingforum.entity.dto.users.RegisterMailDTO;
import com.xingxingforum.entity.dto.users.UserDTO;
import com.xingxingforum.entity.vo.UserInfoVO;
import com.xingxingforum.expcetions.BadRequestException;
import com.xingxingforum.service.IUsersService;
import com.xingxingforum.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.xingxingforum.entity.R;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author huangdada
 * @since 2025-01-22
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户管理")
public class UsersController {
    @Resource
    private MailConfig mailMsg;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Resource
    private IUsersService usersService;
    @Resource
    private OssProperties ossProperties;

    /**
     * @param email 登录邮箱
     * @return 返回R信息
     */
    @ApiOperation(value = "发送邮箱验证码")
    @GetMapping(value = "token/send/{email}")
    public R<Object> sendCode(@PathVariable String email) {
        log.info("邮箱码：{}", email);
        //先从redis中取出验证码信息,看是否有重复的信息
        String code = redisTemplate.opsForValue().get(email);
        if (!StringUtils.isEmpty(code)) {
            return R.ok("验证码已发送，请勿重复发送！");
        }
        //原子操作,因为我们要异步获取短信,因为短信操作会长时间阻塞主线程
        AtomicBoolean res = new AtomicBoolean(false);
        CompletableFuture.runAsync(() -> {
            try {
                res.set(mailMsg.mail(email));
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }).whenComplete((v, e) -> {
            if (e != null) {
                log.error("发送邮件失败：{}", e.getMessage());
            }
            log.info("发送邮件成功：{}", res.get());
        });
        return R.ok(("验证码发送成功！"));
    }

    /**
     *
     * @param registerMailDTO 注册信息
     * @return 返回R
     */
    @ApiOperation(value = "用户注册")
    @PostMapping(value = "token/register")
    public R<Object> login(@RequestBody @Valid RegisterMailDTO registerMailDTO) {
        String code = redisTemplate.opsForValue().get(registerMailDTO.getEmail());
        if (code == null) {
            return R.error("验证码已过期！");
        }
        if (!StringUtils.equals(code, registerMailDTO.getCode())) {
            return R.error("验证码错误！");
        }
        return usersService.register(registerMailDTO);
    }

    /**
     *
     * @param loginFormDTO 登录信息
     * @return R
     */
    @ApiOperation(value = "用户登录")
    @PostMapping(value = "token/login")
    public R<Object> login(@RequestBody @Valid LoginFormDTO loginFormDTO) {
        return usersService.login(loginFormDTO);
    }


    /**
     *
     * @param refreshToken 刷新token
     * @return R
     */
    @ApiOperation("刷新token")
    @GetMapping(value = "token/refresh")
    public R<String> refreshToken(@RequestParam String refreshToken) {
        if(refreshToken == null){
            throw new BadRequestException("登录超时");
        }
        return usersService.refreshToken(refreshToken);
    }

    /**
     *
     * @return 返回七牛云的使用token
     */

    @ApiOperation("获取oss的token")
    @GetMapping(value = "token/oss")
    public R<String> getOssToken() {
        Auth auth = Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
        String ossToken = auth.uploadToken(ossProperties.getBucket());
        if(ossToken == null){
            throw new BadRequestException("获取ossToken失败");
        }
        return R.ok(ossToken);
    }

    /**
     *
     * @param infoDTO 用户信息
     * @return R
     */
    @ApiOperation("初次填写信息")
    @PostMapping(value = "info")
    public R<Object> registerInfo(@RequestBody @Valid InfoDTO infoDTO) {
        return usersService.registerInfo(infoDTO);
    }


    /**
     *
     * @param id 用户id
     * @return 返回用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping(value = "info/{id}")
    public R<UserInfoVO> getUserInfo(@PathVariable Long id) {
        return usersService.getUserInfo(id);
    }
}
