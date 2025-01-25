package com.xingxingforum.entity.dto.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String nickName;
    private String email;
    private String avatar;
    private String bio;
    private int Level;
    private boolean isAdmin;
    private boolean isActive;
    private boolean sex;
    private String ipAddress;
    private String address;
}
