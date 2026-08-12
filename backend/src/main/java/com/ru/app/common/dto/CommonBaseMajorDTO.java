package com.ru.app.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonBaseMajorDTO {
    private Long id;// '专业ID',
    private String majorCode;// '专业编码',
    private String majorName;// '专业名称',
    private Long deptId;// '所属学院ID',
    private String deptName;// '所属学院',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
