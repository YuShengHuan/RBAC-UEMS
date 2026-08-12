package com.ru.app.module.exp.dto;
import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminExpCourseScheduleQueryDTO extends PageQueryDTO {
    private String courseName;//课程名字
    private String className;//班级名字
    private String realName;// '用户真实名',
    private String labLocation;//实验地点
    private Integer isReport;// '是否提交报告(0-有,1-无)',
    private String semester;// '学期(如:2023-2024-1)',
}
