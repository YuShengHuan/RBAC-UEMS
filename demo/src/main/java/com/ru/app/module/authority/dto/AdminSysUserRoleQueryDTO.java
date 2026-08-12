package com.ru.app.module.authority.dto;

import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

/**
 * 用户角色关联返回DTO
 * 包含前端展示所需的核心字段（关联用户名、角色名，避免前端二次查询）
 */
@Data
public class AdminSysUserRoleQueryDTO extends PageQueryDTO {
    private String userAccount;
    private String realName;
    private String roleName;
    private String roleCode;
}