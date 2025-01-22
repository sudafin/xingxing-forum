package com.xingxingforum.entity.dto.admin;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AdminFormDTO {
    @NotNull(message = "账号不能为空")
    private String account;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "验证码不能为空")
    private String code;

    private Boolean rememberMe;
}
