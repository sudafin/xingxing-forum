package com.xingxingforum.service.impl;

import com.xingxingforum.entity.R;
import com.xingxingforum.entity.model.Forums;
import com.xingxingforum.entity.vo.ForumListVO;
import com.xingxingforum.entity.vo.ForumVO;
import com.xingxingforum.mapper.ForumsMapper;
import com.xingxingforum.service.IForumsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingxingforum.utils.BeanUtils;
import com.xingxingforum.utils.CollUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public R<List<ForumListVO>> getForumsList() {
        List<Forums> parentForumsList = lambdaQuery().eq(Forums::getParentId, 0).list();
        if (CollUtils.isEmpty(parentForumsList)) {
            return R.error("当前没有任何板块");
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
}
