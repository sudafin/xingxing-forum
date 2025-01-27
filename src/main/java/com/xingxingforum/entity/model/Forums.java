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
@TableName("forums")
@ApiModel(value="Forums对象", description="")
public class Forums implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "论坛名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "论坛描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "父论坛ID")
    @TableField("parent_id")
    private Long parentId;

    @TableField("post_count")
    private Integer postCount;

    @TableField("thread_count")
    private Integer threadCount;

    @TableField("last_post_id")
    private Long lastPostId;

    @TableField("last_post_time")
    private LocalDateTime lastPostTime;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("avatar")
    private String avatar;

}
