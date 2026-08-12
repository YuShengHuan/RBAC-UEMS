package com.ru.app.common.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonExpLabDTO {
    private Long id;// '实验室ID',
    private String labName;// '实验室名称',
    private String labCode;// '实验室编码',
    private String labLocation;// '实验室地点',
    private Long deptId;//分院ID
    private String deptName;//分院ID
    private Integer campus;//校区
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
