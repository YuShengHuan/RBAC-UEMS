package com.ru.app.module.exp.dto;
import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class FrontExpReportQueryDTO extends PageQueryDTO {
    private String searchContent;
    private String semester;// '学期(如:2023-2024-1)',
}
