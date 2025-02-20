package com.xingxingforum.controller;


import com.xingxingforum.entity.R;
import com.xingxingforum.entity.dto.threads.ThreadQueryDTO;
import com.xingxingforum.entity.page.PageDTO;
import com.xingxingforum.entity.page.PageQuery;
import com.xingxingforum.entity.vo.threads.ThreadListVO;
import com.xingxingforum.service.IThreadsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  帖子表 前端控制器
 * </p>
 *
 * @author huangdada
 * @since 2025-01-22
 */
@RestController
@RequestMapping("/threads")
@Api(tags = "帖子表")
public class ThreadsController {
    @Resource
    private IThreadsService threadsService;

    @ApiOperation(value = "获取帖子列表数据")
    @GetMapping("page")
    public R<PageDTO<ThreadListVO>> threadsPage(ThreadQueryDTO threadQueryDTO) {
        return threadsService.threadsPage(threadQueryDTO);
    }



}
