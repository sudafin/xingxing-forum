package com.xingxingforum.service.impl;

import com.xingxingforum.entity.model.Comments;
import com.xingxingforum.mapper.CommentsMapper;
import com.xingxingforum.service.ICommentsService;
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
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

}
