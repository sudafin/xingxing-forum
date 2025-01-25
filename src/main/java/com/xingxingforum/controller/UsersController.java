package com.xingxingforum.controller;

import com.xingxingforum.config.MailConfig;
import com.xingxingforum.entity.dto.users.LoginFormDTO;
import com.xingxingforum.entity.dto.users.RegisterMailDTO;
import com.xingxingforum.expcetions.BadRequestException;
import com.xingxingforum.service.IUsersService;
import com.xingxingforum.utils.StringUtils;
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
@ApiOperation(value = "用户管理")
public class UsersController {
    @Resource
    private MailConfig mailMsg;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Resource
    private IUsersService usersService;

    /**
     * @param email 登录邮箱
     * @return 返回R信息
     */
    @ApiOperation(value = "发送邮箱验证码")
    @GetMapping(value = "send/{email}")
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

    @ApiOperation(value = "用户注册")
    @PostMapping(value = "register")
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
    @ApiOperation(value = "用户登录")
    @PostMapping(value = "login")
    public R<Object> login(@RequestBody @Valid LoginFormDTO loginFormDTO) {
        return usersService.login(loginFormDTO);
    }
    @ApiOperation("刷新token")
    @GetMapping(value = "refresh")
    public R<String> refreshToken(@RequestParam String refreshToken) {
        if(refreshToken == null){
            throw new BadRequestException("登录超时");
        }
        return usersService.refreshToken(refreshToken);
    }

    @ApiOperation(value = "测试")
    @GetMapping(value = "test")
    public R<Object> test() {
        return R.ok("测试成功！");
    }
}
