package com.xingxingforum.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingxingforum.constants.BadRequestConstant;
import com.xingxingforum.constants.ErrorInfoConstant;
import com.xingxingforum.constants.RedisConstant;
import com.xingxingforum.entity.R;
import com.xingxingforum.entity.dto.threads.ThreadQueryDTO;
import com.xingxingforum.entity.model.*;
import com.xingxingforum.entity.page.PageDTO;
import com.xingxingforum.entity.vo.threads.ThreadListVO;
import com.xingxingforum.expcetions.BadRequestException;
import com.xingxingforum.mapper.*;
import com.xingxingforum.service.IThreadDetailService;
import com.xingxingforum.service.IThreadsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingxingforum.service.IUserPrivacyService;
import com.xingxingforum.utils.ObjectUtils;
import com.xingxingforum.utils.StringUtils;
import com.xingxingforum.utils.UserContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
@Slf4j
public class ThreadsServiceImpl extends ServiceImpl<ThreadsMapper, Threads> implements IThreadsService {
    @Resource
    private IThreadDetailService threadDetailService;
    @Resource
    private UsersMapper usersMapper;
    @Resource
    private ForumsMapper forumsMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private FavoritesMapper favoritesMapper;
    @Resource
    private IUserPrivacyService userPrivacyService;

    //热度 = (likes × 0.5) + (replies × 0.3) + (views × 0.2)

    /**
     * 获取帖子列表
     *
     * @param threadQueryDTO 查询条件
     * @return R
     */
    @Override
    public R<PageDTO<ThreadListVO>> threadsPage(ThreadQueryDTO threadQueryDTO) {
        Page<Threads> threadsQueryPage = new Page<>(threadQueryDTO.getPageNo(), threadQueryDTO.getPageSize());
        Page<ThreadDetail> threadDetailQueryPage = new Page<>(threadQueryDTO.getPageNo(), threadQueryDTO.getPageSize());
        Page<Favorites> threadsFavoriteQueryPage = new Page<>(threadQueryDTO.getPageNo(), threadQueryDTO.getPageSize());
        List<ThreadListVO> threadListVOList;
        //1. 检查是否是主页获取热度列表
        if (ObjectUtils.isNotEmpty(threadQueryDTO.getIsHot())) {
            String hotThreadsKey = RedisConstant.HOT_THREADS + threadQueryDTO.getPageNo() + threadQueryDTO.getPageSize();
            //先查看缓存是否有数据
            String hotThreads = stringRedisTemplate.opsForValue().get(hotThreadsKey);
            if (StringUtils.isNotEmpty(hotThreads)) {
                PageDTO<ThreadListVO> threadListVOPageDTO = JSONUtil.toBean(hotThreads, new TypeReference<>() {
                }, false);
                return R.ok(threadListVOPageDTO);
            }
            Page<ThreadDetail> threadDetailPage = threadDetailService.lambdaQuery().orderByDesc(ThreadDetail::getHotScore).page(threadDetailQueryPage);
            if (threadDetailPage.getTotal() == 0) {
                return R.ok(PageDTO.empty(threadDetailPage));
            }
            threadListVOList = threadDetailConvertThreadListVO(threadDetailPage.getRecords());
            //将threadVOList转为Redis缓存
            PageDTO<ThreadListVO> threadListVOPageDTO = PageDTO.of(threadDetailPage, threadListVOList);
            stringRedisTemplate.opsForValue().set(hotThreadsKey, JSONUtil.toJsonStr(threadListVOPageDTO));
            return R.ok(threadListVOPageDTO);
        }

        //2. 检查是否是用户发的或者是其他用户发的帖子列表,并判断用户点的是自己的贴子还是收藏的帖子
        if (ObjectUtils.isNotEmpty(threadQueryDTO.getUserId())) {
            //2.1 查看查询是否为本人或者其他人
            boolean isMe = threadQueryDTO.getUserId().equals(UserContextUtils.getUser());
            UserPrivacy userPrivacy = null;
            //2.2 获取访问用户的隐私设置
            if (!isMe) {
                userPrivacy = userPrivacyService.getOne(new LambdaQueryWrapper<UserPrivacy>().eq(UserPrivacy::getUserId, threadQueryDTO.getUserId()));
                if (ObjectUtils.isEmpty(userPrivacy)) {
                    log.error(BadRequestConstant.USER_PRIVACY_NOT_EXIST);
                    throw new BadRequestException(ErrorInfoConstant.Msg.CLIENT_ERROR);
                }
            }
            //2.3 处理点击收藏的列表
            if (ObjectUtils.isNotEmpty(threadQueryDTO.getIsFavorite()) &&threadQueryDTO.getIsFavorite()) {
                Page<Favorites> favoritesPage = favoritesMapper.selectPage(threadsFavoriteQueryPage, new LambdaQueryWrapper<Favorites>().eq(Favorites::getUserId, threadQueryDTO.getUserId()));
                List<Long> threadIdList = favoritesPage.getRecords().stream().map(Favorites::getThreadId).collect(Collectors.toList());
                List<ThreadDetail> threadDetailList = threadDetailService.lambdaQuery().in(ThreadDetail::getId, threadIdList).list();
                if (favoritesPage.getTotal() == 0) {
                    return R.ok(PageDTO.empty(favoritesPage));
                }
                //如果是查询本人的收藏列表,不用处理了是否设置私密
                if (!isMe) {
                    if (userPrivacy.getShowFavorite()) {
                        R.error(ErrorInfoConstant.Code.USER_PROFILE_PRIVATE, "访问的用户开启了隐私设置,无法查看该用户的页面");
                    }
                    threadDetailList = threadDetailList.stream().filter(threadDetail -> !threadDetail.getIsHidden()).collect(Collectors.toList());
                }
                threadListVOList = threadDetailConvertThreadListVO(threadDetailList);
                return R.ok(PageDTO.of(favoritesPage, threadListVOList));
            }

            //2.4 处理点击自发贴的列子
            Page<Threads> threadsPage = lambdaQuery().eq(Threads::getUserId, threadQueryDTO.getUserId()).orderByDesc(Threads::getCreatedAt).page(threadsQueryPage);
            if (threadsPage.getTotal() == 0) {
                return R.ok(PageDTO.empty(threadsPage));
            }
            //拿到帖子的详情id
            List<Long> threadDetailIdList = threadsPage.getRecords().stream().map(Threads::getThreadDetailId).collect(Collectors.toList());
            List<ThreadDetail> threadDetailList = threadDetailService.lambdaQuery().in(ThreadDetail::getId, threadDetailIdList).list();
            if (threadDetailList.isEmpty()) {
                return R.ok(PageDTO.empty(threadsPage));
            }
            if (!isMe) {
                //如果开启隐私直接返回错误代码
                if (!userPrivacy.getShowThread()) {
                    return R.error(ErrorInfoConstant.Code.USER_PROFILE_PRIVATE, "访问的用户开启了隐私设置,无法查看该用户的页面");
                }
                //如果没有设置就要将可能隐藏的帖子过滤出来,就是把isHidden为false然后取反留下来
                threadDetailList = threadDetailList.stream().filter(threadDetail -> !threadDetail.getIsHidden()).collect(Collectors.toList());
            }
            threadListVOList = threadDetailConvertThreadListVO(threadDetailList);
            return R.ok(PageDTO.of(threadsPage, threadListVOList));
        }

        // 3.查询板块的列表
        if (ObjectUtils.isEmpty(threadQueryDTO.getForumId())) {
            log.error(BadRequestConstant.FORUM_NOT_EXIST);
            throw new BadRequestException(ErrorInfoConstant.Msg.SERVER_INTER_ERROR);
        }

        return R.ok(null);
    }

    /**
     * 将ThreadDetail转换成ThreadListVO
     *
     * @param records ThreadDetail集合
     * @return ThreadListVO集合
     */
    private List<ThreadListVO> threadDetailConvertThreadListVO(List<ThreadDetail> records) {
        return records.stream().map(threadDetail -> {
            ThreadListVO threadListVO = new ThreadListVO();
            Threads thread = lambdaQuery().eq(Threads::getId, threadDetail.getId()).one();
            if (ObjectUtils.isEmpty(thread)) {
                log.error(BadRequestConstant.THREAD_NOT_EXIST);
                throw new BadRequestException(ErrorInfoConstant.Msg.SERVER_INTER_ERROR);
            }
            Users user = usersMapper.selectById(thread.getUserId());
            if (ObjectUtils.isEmpty(user)) {
                log.error(BadRequestConstant.USER_NOT_EXIST);
                throw new BadRequestException(ErrorInfoConstant.Msg.SERVER_INTER_ERROR);
            }
            Forums forum = forumsMapper.selectById(thread.getForumId());
            if (ObjectUtils.isEmpty(forum)) {
                log.error(BadRequestConstant.FORUM_NOT_EXIST);
                throw new BadRequestException(ErrorInfoConstant.Msg.SERVER_INTER_ERROR);
            }
            threadListVO.setId(thread.getId());
            threadListVO.setSubject(threadListVO.getSubject());
            threadListVO.setContent(thread.getContent());
            threadListVO.setUserName(user.getNickname());
            threadListVO.setUserAvatar(user.getAvatar());
            threadListVO.setForumName(forum.getName());
            threadListVO.setCreateTime(thread.getCreatedAt());
            threadListVO.setReplyCount(threadDetail.getReplyCount());
            threadListVO.setLikeCount(threadDetail.getLikeCount());
            return threadListVO;
        }).collect(Collectors.toList());
    }
}
