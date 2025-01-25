package com.xingxingforum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xingxingforum.constants.BadRequestConstant;

import com.xingxingforum.constants.DBConstant;

import com.xingxingforum.entity.R;
import com.xingxingforum.entity.dto.users.LoginFormDTO;
import com.xingxingforum.entity.dto.users.RegisterMailDTO;
import com.xingxingforum.entity.dto.users.UserDTO;
import com.xingxingforum.entity.model.Users;
import com.xingxingforum.entity.vo.LoginVO;
import com.xingxingforum.expcetions.DbException;
import com.xingxingforum.expcetions.UnauthorizedException;
import com.xingxingforum.mapper.FollowsMapper;
import com.xingxingforum.mapper.UsersMapper;
import com.xingxingforum.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingxingforum.utils.JwtUtils;
import com.xingxingforum.utils.WebUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author huangdada
 * @since 2025-01-22
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    //bcrypt加密的依赖注入
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private FollowsMapper followsMapper;

    @Override
    public R<Object> register(@NotNull RegisterMailDTO registerMailDTO) {
        //判断邮箱是否存在
        Users user = getOne(new LambdaQueryWrapper<Users>().eq(Users::getEmail, registerMailDTO.getEmail()));
        if(user != null){
            return R.error("邮箱已存在");
        }
        user = new Users();
        String encodePassword = passwordEncoder.encode(registerMailDTO.getPassword());
        user.setPassword(encodePassword);
        user.setEmail(registerMailDTO.getEmail());
        boolean save = save(user);
        if(!save){
            throw new DbException(DBConstant.DATA_SAVE_ERROR);
        }
        return R.ok(null);
    }

    @Override
    public R<Object> login(@NotNull LoginFormDTO loginFormDTO) {
        //判断邮箱是否存在
        Users user = getOne(new LambdaQueryWrapper<Users>().eq(Users::getEmail, loginFormDTO.getEmail()));
        if(user == null){
            return R.error(BadRequestConstant.USER_ACCOUNT_PASSWORD_ERROR);
        }
        //密码校验
        if(!passwordEncoder.matches(loginFormDTO.getPassword(), user.getPassword())){
            return R.error(BadRequestConstant.USER_ACCOUNT_PASSWORD_ERROR);
        }
        String token;
        UserDTO userDTO = UserDTO.builder().id(user.getId()).
                nickName(user.getNickname()).
                email(user.getEmail()).
                avatar(user.getAvatar()).
                bio(user.getBio()).
                isAdmin(user.getIsAdmin()).
                isActive(user.getIsActive()).
                build();
        try {
            //生成token
            token = jwtUtils.createToken(userDTO);
        } catch (Exception e) {
            log.error("生成token失败", e);
            throw new UnauthorizedException(BadRequestConstant.TOKEN_GENERATE_FAILED);
        }
        return R.ok(LoginVO.builder().userDTO(userDTO).token(token).build());
    }
}
