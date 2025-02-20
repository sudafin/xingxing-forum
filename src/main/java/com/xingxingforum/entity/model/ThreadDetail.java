package com.xingxingforum.entity.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 
 * </p>
 *
 * @author huangdada
 * @since 2025-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("thread_detail")
@ApiModel(value="ThreadDetail对象", description="")
public class ThreadDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("hot_score")
    private Float hotScore;

    @TableField("like_count")
    private Integer likeCount;

    @TableField("reply_count")
    private Integer replyCount;

    @TableField("view_count")
    private Integer viewCount;

    @ApiModelProperty(value = "是否锁定")
    @TableField("is_locked")
    private Boolean isLocked;

    @ApiModelProperty(value = "是否删除")
    @TableField("is_deleted")
    private Boolean isDeleted;

    @ApiModelProperty(value = "是否置顶")
    @TableField("is_top")
    private Boolean isTop;

    @ApiModelProperty(value = "是否精华")
    @TableField("is_essence")
    private Boolean isEssence;

    @ApiModelProperty(value = "是否隐藏")
    @TableField("is_hidden")
    private Boolean isHidden;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;


}
