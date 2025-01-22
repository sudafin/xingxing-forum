package com.xingxingforum.service.impl;

import com.xingxingforum.entity.model.Users;
import com.xingxingforum.mapper.UsersMapper;
import com.xingxingforum.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
