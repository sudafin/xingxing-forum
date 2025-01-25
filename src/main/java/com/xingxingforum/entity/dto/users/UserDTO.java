package com.xingxingforum.entity.dto.users;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String nickName;
    private String email;
    private String avatar;
    private String bio;
    private Integer Level;
    private Boolean isAdmin;
    private Boolean isActive;
    private Integer sex;
    private String ipAddress;
    private String address;
    private LocalDateTime birthday;
    private String profession;
    private String school;

}
