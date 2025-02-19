package com.xingxingforum.service;

import com.xingxingforum.entity.R;
import com.xingxingforum.entity.model.Forums;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingxingforum.entity.vo.forums.ForumListVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author huangdada
 * @since 2025-01-22
 */
public interface IForumsService extends IService<Forums> {

    R<List<ForumListVO>> getForumsList();

    R<Object> favoriteForum(Long forumId, Integer favorite);
}
