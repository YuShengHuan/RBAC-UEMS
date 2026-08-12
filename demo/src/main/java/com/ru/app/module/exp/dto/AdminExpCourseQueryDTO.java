package com.ru.app.module.exp.dto;
import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminExpCourseQueryDTO extends PageQueryDTO {
    private String courseCode;// '课程编码',
    private String courseName;// '课程名称',
    private Integer courseType;//课程类型(1-必修,2-选修)
    private String deptName;// '所属学院',
}
