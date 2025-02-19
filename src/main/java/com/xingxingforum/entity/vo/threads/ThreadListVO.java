package com.xingxingforum.entity.vo.threads;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ThreadListVO {
    private Long id;
    private String subject;
    private String content;
    private String userName;
    private String userAvatar;
    private String forumName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Integer replyCount;
    private Integer likeCount;
}
