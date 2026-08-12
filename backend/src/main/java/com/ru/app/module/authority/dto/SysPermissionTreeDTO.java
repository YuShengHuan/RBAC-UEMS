package com.ru.app.module.authority.dto;

import com.ru.app.common.entity.SysPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限树形结构DTO（继承基础实体，新增子节点列表）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPermissionTreeDTO extends SysPermissionWithOwnDTO {
    // 子权限节点
    private List<SysPermissionTreeDTO> children = new ArrayList<>();
}