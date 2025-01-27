package com.xingxingforum.entity.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.xingxingforum.enums.RelationEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户与版块的关联关系表（支持关注/版主等关系）
 * </p>
 *
 * @author huangdada
 * @since 2025-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_forum_relations")
@ApiModel(value="UserForumRelations对象", description="用户与版块的关联关系表（支持关注/版主等关系）")
public class UserForumRelations implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;

    @ApiModelProperty(value = "版块ID")
    @TableField("forum_id")
    private Long forumId;

    @ApiModelProperty(value = "关系类型（1: 普通关注 2: 版主等）")
    @TableField("relation_type")
    private RelationEnum relationType;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;


}
