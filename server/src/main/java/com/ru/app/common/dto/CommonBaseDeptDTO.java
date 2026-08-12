package com.ru.app.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonBaseDeptDTO {
    private Long id;//  '学院ID',
    private String deptCode;// '学院编码',
    private String deptName;// '学院名称',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
