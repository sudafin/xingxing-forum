package com.xingxingforum.service;

import com.xingxingforum.entity.R;
import com.xingxingforum.entity.dto.users.InfoDTO;
import com.xingxingforum.entity.dto.users.LoginFormDTO;
import com.xingxingforum.entity.dto.users.RegisterMailDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingxingforum.entity.model.Users;
import com.xingxingforum.entity.vo.UserInfoVO;

import javax.validation.Valid;

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

    R<String> refreshToken(String refreshToken);

    R<Object> registerInfo(@Valid InfoDTO infoDTO);
    R<UserInfoVO> getUserInfo(Long id);
}
