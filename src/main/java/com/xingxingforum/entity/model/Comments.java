package com.xingxingforum.entity.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2025-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("comments")
@ApiModel(value="Comments对象", description="")
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;
     
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @ApiModelProperty(value = "帖子id")
    @TableField("thread_id")
    private Long threadId;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "父评论id")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "评论内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "评论图片")
    @TableField("images")
    private String images;

    @ApiModelProperty(value = "点赞数")
    @TableField("likes")
    private Integer likes;

    @ApiModelProperty(value = "是否是第一级评论")
    @TableField("is_first")
    private Boolean isFirst;

    @ApiModelProperty(value = "是否删除")
    @TableField("is_deleted")
    private Boolean isDeleted;

    @ApiModelProperty(value = "是否编辑")
    @TableField("is_edited")
    private Boolean isEdited;

    @ApiModelProperty(value = "编辑时间")
    @TableField("edit_time")
    private LocalDateTime editTime;

    @ApiModelProperty(value = "创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    @TableField("updated_at")
    private LocalDateTime updatedAt;


}
