package com.ru.app.common.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonExpCourseDTO {
    private Long id;// '课程ID（UUID）',
    private String courseCode;// '课程编码',
    private String courseName;// '课程名称',
    private Integer courseType;//课程类型(1-必修,2-选修)
    private Long deptId;// '所属学院id',
    private String deptName;// '所属学院',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
