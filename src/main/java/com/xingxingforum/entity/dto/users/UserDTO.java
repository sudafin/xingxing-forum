package com.xingxingforum.entity.dto.users;

import com.xingxingforum.enums.SexEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
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
    private SexEnum sex;
    private String ipAddress;
    private String address;
    private LocalDate birthday;
    private String profession;
    private String school;

}
