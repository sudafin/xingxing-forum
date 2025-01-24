package com.xingxingforum.service;

import com.xingxingforum.entity.R;
import com.xingxingforum.entity.dto.users.LoginFormDTO;
import com.xingxingforum.entity.dto.users.RegisterMailDTO;
import com.xingxingforum.entity.model.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author huangdada
 * @since 2025-01-22
 */
public interface IUsersService extends IService<Users> {

    R<Object> register(RegisterMailDTO registerMailDTO);

    R<Object> login(LoginFormDTO loginFormDTO);
}
