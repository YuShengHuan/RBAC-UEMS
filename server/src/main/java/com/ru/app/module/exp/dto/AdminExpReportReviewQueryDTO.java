package com.ru.app.module.exp.dto;
import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminExpReportReviewQueryDTO extends PageQueryDTO {
    private String semester;// '学期(如:2023-2024-1)',
    private String courseName;//课程名字
    private String projectName;// '实验项目ID',
    private String className;//班级名字
    private String uploadRealName;// '上传用户的真实名',,
    private String reviewRealName;// '批改用户的真实名',
}
