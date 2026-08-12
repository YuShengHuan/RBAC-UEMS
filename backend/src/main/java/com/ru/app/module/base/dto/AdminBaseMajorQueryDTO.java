package com.ru.app.module.base.dto;

import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminBaseMajorQueryDTO extends PageQueryDTO {
    private String majorCode;// '专业编码',
    private String majorName;// '专业名称',
}
