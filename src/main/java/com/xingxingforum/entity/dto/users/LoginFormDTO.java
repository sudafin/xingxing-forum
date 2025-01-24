package com.xingxingforum.entity.dto.users;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginFormDTO {
    @NotNull(message = "邮箱不能为空")
    private String email;

    @NotNull(message = "密码不能为空")
    private String password;
}
