package com.xingxingforum.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.xingxingforum.expcetions.BadRequestException;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum RelationEnum implements BaseEnum {
    NORMAL(1, "普通关注"),
    MODERATOR(2, "版主"),
    ;
    @EnumValue
    @JsonValue
    final int value;
    final String desc;

    RelationEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    public static RelationEnum of(Integer value) {
        for (RelationEnum type : RelationEnum.values()) {
            if (Objects.equals(type.getValue(), value)) {
                return type;
            }
        }
        throw new BadRequestException(400, "关系类型不存在");
    }
}
