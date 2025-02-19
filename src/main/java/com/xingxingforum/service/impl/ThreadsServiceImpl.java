package com.xingxingforum.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingxingforum.constants.BadRequestConstant;
import com.xingxingforum.constants.ErrorInfoConstant;
import com.xingxingforum.entity.R;
import com.xingxingforum.entity.dto.threads.ThreadQueryDTO;
import com.xingxingforum.entity.model.Forums;
import com.xingxingforum.entity.model.ThreadDetail;
import com.xingxingforum.entity.model.Threads;
import com.xingxingforum.entity.model.Users;
import com.xingxingforum.entity.page.PageDTO;
import com.xingxingforum.entity.vo.threads.ThreadListVO;
import com.xingxingforum.expcetions.BadRequestException;
import com.xingxingforum.mapper.ForumsMapper;
import com.xingxingforum.mapper.ThreadsMapper;
import com.xingxingforum.mapper.UsersMapper;
import com.xingxingforum.service.IThreadDetailService;
import com.xingxingforum.service.IThreadsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingxingforum.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author huangdada
 * @since 2025-01-22
 */
@Service
public class ThreadsServiceImpl extends ServiceImpl<ThreadsMapper, Threads> implements IThreadsService {
    @Resource
    private IThreadDetailService threadDetailService;
    @Resource
    private UsersMapper usersMapper;
    @Resource
    private ForumsMapper forumsMapper;

    //热度 = (likes × 0.5) + (replies × 0.3) + (views × 0.2)

    /**
     * 获取帖子列表
     * @param threadQueryDTO 查询条件
     * @return R
     */
    @Override
    public R<PageDTO<ThreadListVO>> threadsPage(ThreadQueryDTO threadQueryDTO) {
        Page<Threads> threadsQueryPage = new Page<>(threadQueryDTO.getPageNo(), threadQueryDTO.getPageSize());
        Page<ThreadDetail> threadDetailQueryPage = new Page<>(threadQueryDTO.getPageNo(), threadQueryDTO.getPageSize());

        //1. 检查是否是获取热度列表
        if (ObjectUtils.isNotEmpty(threadQueryDTO.getIsHot())) {
            Page<ThreadDetail> threadDetailPage = threadDetailService.lambdaQuery().orderByDesc(ThreadDetail::getHotScore).page(threadDetailQueryPage);
            if (threadDetailPage.getTotal() == 0) {
                return R.ok(PageDTO.empty(threadDetailPage));
            }
            List<ThreadListVO> threadListVOList = threadDeatilConvertThreadListVO(threadDetailPage.getRecords());
            return R.ok(PageDTO.of(threadDetailPage, threadListVOList));
        }
        //2. 检查是否是用户帖子列表
        if(threadQueryDTO.getIsMember()) {

        }
        Page<Threads> page = lambdaQuery()
                .eq(Threads::getIsDeleted, false)
                .eq(Threads::getIsLocked, false)
                .eq(ObjectUtils.isNotEmpty(threadQueryDTO.getForumId()), Threads::getForumId, threadQueryDTO.getForumId())
                .eq(ObjectUtils.isNotEmpty(threadQueryDTO.getIsTop()), Threads::getIsTop, threadQueryDTO.getIsTop())
                .eq(ObjectUtils.isNotEmpty(threadQueryDTO.getIsEssence()), Threads::getIsEssence, threadQueryDTO.getIsEssence())
                .orderByDesc(threadQueryDTO.getIsLatestReviewsOrder(), Threads::getUpdatedAt)
                .orderByDesc(!threadQueryDTO.getIsLatestReviewsOrder(), Threads::getCreatedAt)
                .page(threadsQueryPage);

        return R.ok(null);
    }

    /**
     * 将ThreadDetail转换成ThreadListVO
     * @param records ThreadDetail集合
     * @return ThreadListVO集合
     */
    private List<ThreadListVO> threadDeatilConvertThreadListVO(List<ThreadDetail> records) {
        return records.stream().map(threadDetail -> {
            ThreadListVO threadListVO = new ThreadListVO();
            Threads thread = lambdaQuery().eq(Threads::getId, threadDetail.getId()).one();
            if(ObjectUtils.isEmpty(thread)){
                log.error(BadRequestConstant.THREAD_NOT_EXIST);
                throw new BadRequestException(ErrorInfoConstant.Msg.SERVER_INTER_ERROR);
            }
            Users user = usersMapper.selectById(thread.getUserId());
            if(ObjectUtils.isEmpty(user)){
                log.error(BadRequestConstant.User_NOT_EXIST);
                throw new BadRequestException(ErrorInfoConstant.Msg.SERVER_INTER_ERROR);
            }
            Forums forum = forumsMapper.selectById(thread.getForumId());
            if(ObjectUtils.isEmpty(forum)){
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
