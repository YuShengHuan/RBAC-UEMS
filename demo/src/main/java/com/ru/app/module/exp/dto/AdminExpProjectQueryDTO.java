package com.ru.app.module.exp.dto;
import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminExpProjectQueryDTO extends PageQueryDTO {
    private String projectCode;// '实验编码',
    private String projectName; //'实验名称',
    private String semester;// '学期(如:2023-2024-1)',
    private String courseName;//课程名字
}
