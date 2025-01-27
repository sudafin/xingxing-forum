package com.xingxingforum.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ForumListVO  implements Serializable {
    private ForumVO parentForumVO;
    private List<ForumVO> childrenForumVOList;
}
