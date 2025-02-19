package com.xingxingforum.service;

import com.xingxingforum.entity.R;
import com.xingxingforum.entity.dto.threads.ThreadQueryDTO;
import com.xingxingforum.entity.model.Threads;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingxingforum.entity.page.PageDTO;
import com.xingxingforum.entity.page.PageQuery;
import com.xingxingforum.entity.vo.threads.ThreadListVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author huangdada
 * @since 2025-01-22
 */
public interface IThreadsService extends IService<Threads> {

    R<PageDTO<ThreadListVO>> threadsPage(ThreadQueryDTO threadQueryDTO);
}
