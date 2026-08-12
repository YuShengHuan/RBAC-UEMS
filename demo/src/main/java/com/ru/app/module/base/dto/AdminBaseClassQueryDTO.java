package com.ru.app.module.base.dto;

import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminBaseClassQueryDTO extends PageQueryDTO {
    private String classCode;// '班级编码',
    private String className;// '班级名称',
}
