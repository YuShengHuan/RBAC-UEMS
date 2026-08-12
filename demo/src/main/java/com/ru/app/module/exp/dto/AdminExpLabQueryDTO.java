package com.ru.app.module.exp.dto;
import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminExpLabQueryDTO extends PageQueryDTO {
    private String labCode;// '实验室编码',
    private String labName;// '实验室名称',
    private String labLocation;// '实验室地点',
    private String deptName;//分院
}
