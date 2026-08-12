package com.ru.app.module.base.dto;

import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminBaseDeptQueryDTO extends PageQueryDTO {
    private String deptCode;// '学院编码',
    private String deptName;// '学院名称',
    private Integer campus;// '校区（1-本部，2-其他）',
}
