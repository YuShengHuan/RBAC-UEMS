package com.ru.app.module.authority.dto;
import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminSysRoleQueryDTO extends PageQueryDTO {
    private String roleCode;// '角色编码',
    private String roleName;// '角色名称',
}
