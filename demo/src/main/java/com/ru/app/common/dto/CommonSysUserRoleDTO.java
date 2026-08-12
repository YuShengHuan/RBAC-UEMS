package com.ru.app.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户角色关联返回DTO
 * 包含前端展示所需的核心字段（关联用户名、角色名，避免前端二次查询）
 */
@Data
public class CommonSysUserRoleDTO {
    private Long id;
    private Long userId;
    private String userAccount;
    private String realName;
    private Long roleId;
    private String roleName;
    private String roleCode;
    private Long deptId;
    private String deptName;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}