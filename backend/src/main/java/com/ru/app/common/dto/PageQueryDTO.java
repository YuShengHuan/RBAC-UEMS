package com.ru.app.common.dto;

import lombok.Data;

@Data
public class PageQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
