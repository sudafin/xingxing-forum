package com.xingxingforum.entity.dto.users;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class InfoDTO {
    @NotNull(message = "生日不能为空")
    private String userName;

    @NotNull(message = "生日不能为空")
    private LocalDate birthday;

    @NotNull(message = "性别不能为空")
    private String sex;

    private String avatar;
    private String bio;
    private String occupation;
    private String school;
    private List<String> interestModules;

}
