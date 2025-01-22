package com.xingxingforum.entity.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2025-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("threads")
@ApiModel(value="Threads对象", description="")
public class Threads implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "论坛ID")
    @TableField("forum_id")
    private Long forumId;

    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "帖子主题")
    @TableField("subject")
    private String subject;

    @TableField("content")
    private String content;

    @TableField("images")
    private String images;

    @TableField("replies")
    private Integer replies;

    @TableField("views")
    private Integer views;

    @TableField("likes")
    private Integer likes;

    @TableField("is_top")
    private Boolean isTop;

    @TableField("is_essence")
    private Boolean isEssence;

    @TableField("is_locked")
    private Boolean isLocked;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;


}
