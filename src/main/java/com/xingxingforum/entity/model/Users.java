package com.xingxingforum.entity.model;

import com.baomidou.mybatisplus.annotation.TableField;
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

/**
 * <p>
 * 
 * </p>
 *
 * @author huangdada
 * @since 2025-01-25
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

    @TableField("email")
    private String email;

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

    @ApiModelProperty(value = "性别")
    @TableField("sex")
    private Integer sex;

    @ApiModelProperty(value = "ip地址")
    @TableField("ip_address")
    private String ipAddress;

    @ApiModelProperty(value = "个人地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "等级")
    @TableField("level")
    private Integer level;

    @ApiModelProperty(value = "生日")
    @TableField("birthday")
    private LocalDateTime birthday;

    @ApiModelProperty(value = "职业")
    @TableField("profession")
    private String profession;

    @TableField("school")
    private String school;


}
