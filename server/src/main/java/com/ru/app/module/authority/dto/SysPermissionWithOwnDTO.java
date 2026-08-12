package com.ru.app.module.authority.dto;

import com.ru.app.common.entity.SysPermission;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 权限+角色拥有标记的中间DTO（数据库查询结果直接映射）
 */
@Data
public class SysPermissionWithOwnDTO extends SysPermission {
    private Integer isOwn; // 角色是否拥有该权限：1=是，0=否
}