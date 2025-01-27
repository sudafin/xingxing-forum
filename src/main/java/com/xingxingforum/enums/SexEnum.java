package com.xingxingforum.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.xingxingforum.expcetions.BadRequestException;
import lombok.Getter;
import java.util.Objects;

@Getter
public enum SexEnum implements BaseEnum{
    Male(0, "男"),
    FEMALE(1, "女"),
    ;
    @EnumValue
    @JsonValue
    final int value;
    final String desc;

    SexEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static SexEnum of(String desc) {
        for (SexEnum type : SexEnum.values()) {
            if (Objects.equals(type.getDesc(), desc)) {
                return type;
            }
        }
        throw new BadRequestException(400, "性别类型不存在");
    }
//    public static List<String> getUserTypeList() {
//        ArrayList<String> userTypeList = new ArrayList<>();
//        for (SexEnum type : SexEnum.values()) {
//            //按照顺序添加
//            userTypeList.add(type.getDesc());
//        }
//        return userTypeList;
//    }
}
