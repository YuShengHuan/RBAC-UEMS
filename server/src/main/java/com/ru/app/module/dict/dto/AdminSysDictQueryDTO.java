package com.ru.app.module.dict.dto;

import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

/**
 * 字典分页查询参数DTO
 */
@Data
public class AdminSysDictQueryDTO extends PageQueryDTO {
    private String dictGroup;       // 字典分组（模糊查询，如“weather”）
    private String dictKey;        // 字典键（模糊查询，如“sun”）
    private String dictValue;      // 字典值（模糊查询，如“晴天”）
}