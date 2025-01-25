package com.xingxingforum.entity.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 
 * </p>
 *
 * @author huangdada
 * @since 2025-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("users")
@ApiModel(value="Users对象", description="")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @TableField("avatar")
    private String avatar;

    @TableField("nickname")
    private String nickname;

    @TableField("bio")
    private String bio;

    @TableField("last_login_at")
    private LocalDateTime lastLoginAt;

    @TableField("is_active")
    private Boolean isActive;

    @TableField("is_admin")
    private Boolean isAdmin;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("sex")
    private Boolean sex;

    @TableField("ip_address")
    private String ipAddress;

    @TableField("address")
    private String address;

    @TableField("level")
    private Integer level;

}
