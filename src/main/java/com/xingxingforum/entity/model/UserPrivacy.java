package com.xingxingforum.entity.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户隐私设置表
 * </p>
 *
 * @author huangdada
 * @since 2025-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_privacy")
@ApiModel(value="UserPrivacy对象", description="用户隐私设置表")
public class UserPrivacy implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "用户ID（关联users.id）")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "帖子可见性")
    @TableField("show_thread")
    private Boolean showThread;

    @ApiModelProperty(value = "收藏可见性")
    @TableField("show_favorite")
    private Boolean showFavorite;

    @ApiModelProperty(value = "职业可见性")
    @TableField("show_profession")
    private Boolean showProfession;

    @ApiModelProperty(value = "学校可见性")
    @TableField("show_school")
    private Boolean showSchool;

    @ApiModelProperty(value = "允许被搜索（0:不允许 1:允许）")
    @TableField("allow_search")
    private Boolean allowSearch;

    @ApiModelProperty(value = "允许私信（0:不允许 1:允许）")
    @TableField("allow_message")
    private Boolean allowMessage;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;


}
