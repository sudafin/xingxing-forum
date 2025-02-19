package com.xingxingforum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xingxingforum.constants.BadRequestConstant;
import com.xingxingforum.constants.ErrorInfoConstant;
import com.xingxingforum.entity.R;
import com.xingxingforum.entity.model.Forums;
import com.xingxingforum.entity.model.UserForumRelations;
import com.xingxingforum.entity.vo.forums.ForumListVO;
import com.xingxingforum.entity.vo.forums.ForumVO;
import com.xingxingforum.enums.RelationEnum;
import com.xingxingforum.expcetions.BadRequestException;
import com.xingxingforum.mapper.ForumsMapper;
import com.xingxingforum.mapper.UserForumRelationsMapper;
import com.xingxingforum.service.IForumsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingxingforum.utils.BeanUtils;
import com.xingxingforum.utils.CollUtils;
import com.xingxingforum.utils.ObjectUtils;
import com.xingxingforum.utils.UserContextUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author huangdada
 * @since 2025-01-22
 */
@Service
public class ForumsServiceImpl extends ServiceImpl<ForumsMapper, Forums> implements IForumsService {
    @Resource
    private UserForumRelationsMapper userForumRelationsMapper;
    @Override
    public R<List<ForumListVO>> getForumsList() {
        List<Forums> parentForumsList = lambdaQuery().eq(Forums::getParentId, 0).list();
        if (CollUtils.isEmpty(parentForumsList)) {
            log.error(BadRequestConstant.FORUM_NOT_EXIST);
            throw new BadRequestException(ErrorInfoConstant.Msg.SERVER_INTER_ERROR);
        }
        //返回App的数据
        ArrayList<ForumListVO> forumListVOArrayList = new ArrayList<ForumListVO>();
        for (Forums parent : parentForumsList) {
             // 设置单个对象集合对象存放父板块信息和子模块信息
            ForumListVO forumListVO = new ForumListVO();
            // 设置父板块信息
            ForumVO parentForumVO = BeanUtils.copyBean(parent, ForumVO.class);
            forumListVO.setParentForumVO(parentForumVO);
            // 设置子板块信息
            List<ForumVO> childForumVOS = BeanUtils.copyList(lambdaQuery().eq(Forums::getParentId, parent.getId()).list(), ForumVO.class);
            forumListVO.setChildrenForumVOList(childForumVOS);
            // 添加到集合中
            forumListVOArrayList.add(forumListVO);
        }
        return R.ok(forumListVOArrayList);
    }

    @Override
    public R<Object> favoriteForum(Long forumId, Integer favorite) {
        Forums forum = getById(forumId);
        //查看板块是否存在
        if(ObjectUtils.isEmpty(forum)){
            log.error(BadRequestConstant.FORUM_NOT_EXIST);
            throw new BadRequestException(ErrorInfoConstant.Msg.SERVER_INTER_ERROR);
        }
        //只有模块才能操作
        if(forum.getParentId() == 0){
            log.error(BadRequestConstant.PARENT_FORUM_NOT_EXIST);
            throw new BadRequestException(ErrorInfoConstant.Msg.CLIENT_ERROR);
        }
        //查看板块与用户的关系
        Long userId = UserContextUtils.getUser();
        UserForumRelations userForumRelation = userForumRelationsMapper.selectOne(new LambdaQueryWrapper<UserForumRelations>().eq(UserForumRelations::getUserId, userId).eq(UserForumRelations::getForumId, forumId));
        //如果没有点赞过就新增点赞表
        if(ObjectUtils.isEmpty(userForumRelation)) {
            userForumRelation.setUserId(userId);
            userForumRelation.setForumId(forumId);
            userForumRelation.setRelationType(RelationEnum.FOLLOWER);
            userForumRelationsMapper.insert(userForumRelation);
        }
        //查看关系和favorite的值, 如果是喜爱或取消操作则关系需要为false/true, 如果不为false/true说明已经被点赞或取消了即发生冲突
        if(userForumRelation.getRelationType().equalsValue(favorite)){
            log.error(BadRequestConstant.FORUM__FAVORITE_CONFLICT);
            throw new BadRequestException(ErrorInfoConstant.Msg.CLIENT_ERROR);
        }
        //最后修改关系
        userForumRelation.setRelationType(RelationEnum.of(favorite));
        userForumRelationsMapper.updateById(userForumRelation);
        return R.ok(null);
    }
}
