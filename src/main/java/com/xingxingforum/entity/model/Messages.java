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
@TableName("messages")
@ApiModel(value="Messages对象", description="")
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会话ID")
    @TableField("conversation_id")
    private Long conversationId;

    @ApiModelProperty(value = "发送者ID")
    @TableField("from_user_id")
    private Long fromUserId;

    @ApiModelProperty(value = "消息内容")
    @TableField("content")
    private String content;

    @TableField("type")
    private String type;

    @TableField("is_read")
    private Boolean isRead;

    @TableField("created_at")
    private LocalDateTime createdAt;


}
