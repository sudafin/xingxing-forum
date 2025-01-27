package com.xingxingforum.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ForumVO  implements Serializable {
    @ApiModelProperty(value = "论坛ID")
    private Long id;
    @ApiModelProperty(value = "论坛名")
    private String name;
    @ApiModelProperty(value = "论坛描述")
    private String description;
    @ApiModelProperty(value = "论坛头像")
    private String avatar;
    @ApiModelProperty(value = "父论坛ID")
    private Long parentId;
    @ApiModelProperty(value = "帖子/回复数")
    private Integer postCount;
    @ApiModelProperty(value = "主题数")
    private Integer threadCount;
    @ApiModelProperty(value = "最后发帖时间")
    private Long lastPostId;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime lastPostTime;

}
