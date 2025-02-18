package com.xingxingforum.entity.vo;

import com.xingxingforum.entity.dto.users.UserDTO;
import com.xingxingforum.enums.SexEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
//TODO 还需要添加帖子等属性
public class UserInfoVO {
    private Long id;
    private String nickname;
    private String email;
    private String avatar;
    private String bio;
    private Integer level;
    private Boolean isAdmin;
    private SexEnum sex;
    private String ipAddress;
    private String address;
    private LocalDate birthday;
    private String profession;
    private String school;
    private Long followCount;
    private Long fansCount;
    private Long likeCount;
}
