package com.ru.app.module.exp.dto;
import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminExpTeachingCoreQueryDTO extends PageQueryDTO {
    private String courseName;//课程名字
    private String className;//班级名字
    private String realName;// '用户真实名',
    private String semester;// '学期(如:2023-2024-1)',
}
