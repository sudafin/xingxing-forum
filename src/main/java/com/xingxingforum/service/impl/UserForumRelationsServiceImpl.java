package com.xingxingforum.service.impl;

import com.xingxingforum.entity.model.UserForumRelations;
import com.xingxingforum.mapper.UserForumRelationsMapper;
import com.xingxingforum.service.IUserForumRelationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户与版块的关联关系表（支持关注/版主等关系） 服务实现类
 * </p>
 *
 * @author huangdada
 * @since 2025-01-27
 */
@Service
public class UserForumRelationsServiceImpl extends ServiceImpl<UserForumRelationsMapper, UserForumRelations> implements IUserForumRelationsService {

}
