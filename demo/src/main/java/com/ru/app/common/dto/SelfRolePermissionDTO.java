package com.ru.app.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * 角色-权限VO（匹配目标返回格式）
 */
@Data
@JsonIgnoreProperties({"roleId"}) // 忽略roleId字段
public class SelfRolePermissionDTO {
    private Long roleId; // 新增：角色主键ID（与数据库r.id对应）
    private String roleCode; // 角色编码
    private String  roleName;//角色名字
    private List<String> permCodes; // 权限编码列表
}