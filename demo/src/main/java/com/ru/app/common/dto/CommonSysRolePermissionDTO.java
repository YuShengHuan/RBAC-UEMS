package com.ru.app.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色权限关联返回DTO
 * 包含前端展示所需的核心字段（关联角色名、权限名，避免前端二次查询）
 */
@Data
public class CommonSysRolePermissionDTO {
    private Long id;
    private Long roleId;
    private String roleName;
    private String roleCode;
    private Long permId;
    private String permName;
    private String permCode;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}