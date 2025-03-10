package com.xingxingforum.controller;


import com.xingxingforum.entity.R;
import com.xingxingforum.entity.vo.forums.ForumListVO;
import com.xingxingforum.service.IForumsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author huangdada
 * @since 2025-01-22
 */
@RestController
@RequestMapping("/forum")
@Api(tags = "版块管理")
public class ForumsController {
    @Resource
    private IForumsService forumsService;

    @ApiOperation(value = "获取版块列表")
    @GetMapping("list")
    public R<List<ForumListVO>> getForumsList() {
        return forumsService.getForumsList();
    }

    @ApiOperation(value = "添加或取消喜爱的板块")
    @GetMapping("favorite/{id}")
    public R<Object> favoriteForum(@PathVariable("id") Long forumId, Integer isFavorite) {
        return forumsService.favoriteForum(forumId, isFavorite);
    }

}
