package com.xingxingforum.service.impl;

import com.xingxingforum.entity.model.Messages;
import com.xingxingforum.mapper.MessagesMapper;
import com.xingxingforum.service.IMessagesService;
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
public class MessagesServiceImpl extends ServiceImpl<MessagesMapper, Messages> implements IMessagesService {

}
