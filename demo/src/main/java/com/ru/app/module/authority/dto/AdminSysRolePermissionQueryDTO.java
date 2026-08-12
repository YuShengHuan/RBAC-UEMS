package com.ru.app.module.authority.dto;

import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

/**
 * 角色权限关联返回DTO
 * 包含前端展示所需的核心字段（关联角色名、权限名，避免前端二次查询）
 */
@Data
public class AdminSysRolePermissionQueryDTO extends PageQueryDTO {
    private String roleName;
    private String roleCode;
    private String permName;
    private String permCode;
}