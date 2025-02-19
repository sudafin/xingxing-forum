package com.xingxingforum.entity.dto.threads;

import com.xingxingforum.entity.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.boot.context.properties.bind.DefaultValue;


@EqualsAndHashCode(callSuper = true)
//使用GET请求所以需要使用@Data注释,不能单纯用@Getter,不然传过来的数据Spring不会设置好
@Data
public class ThreadQueryDTO extends PageQuery {
    private Long forumId;
    private Long userId;
    private Boolean isTop;
    private Boolean isHot;
    private Boolean isEssence;
    private Boolean isFavorite;
    @ApiModelProperty(value = "是否按最新评论排序")
    private Boolean isLatestReviewsOrder;

}
