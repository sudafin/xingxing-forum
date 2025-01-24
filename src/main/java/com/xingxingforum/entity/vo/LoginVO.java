package com.xingxingforum.entity.vo;


import com.xingxingforum.entity.dto.users.UserDTO;
import lombok.Builder;
import lombok.Data;


import java.io.Serializable;

@Data
@Builder
public class LoginVO implements Serializable {
    private static final long serialVersionUID = -3124612657759050173L;
    private UserDTO userDTO;
    private String token;
}
