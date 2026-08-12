package com.ru.app.common.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommonBaseClassDTO {
    private Long id;// '班级ID',
    private String classCode;// '班级编码',
    private String className;// '班级名称',
    private Integer grade; //'年级(如:2023级)',
    private Long studentCount;// '班级人数',
    private Long majorId;// '专业ID',
    private String majorName;// '专业名字',
    private Long deptId;// '所属学院ID',
    private String deptName;// '所属学院',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
