package com.ru.app.module.authority.dto;
import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminSysPermissionQueryDTO extends PageQueryDTO {
    private String permCode;// '权限编码
    private String permName;//  '权限名称'
    private Integer permType; //'权限类型(1:视图,2:操作)'
}
