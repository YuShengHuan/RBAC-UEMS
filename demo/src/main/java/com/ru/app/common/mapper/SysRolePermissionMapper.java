package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysRolePermissionDTO;
import com.ru.app.common.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {
    @Select("""
        SELECT 
            rp.id,
            r.id AS roleId,
            r.role_name AS roleName,   
            r.role_code AS roleCode,
            p.id AS permId,
            p.perm_name AS permName,  
            p.perm_code AS permCode,
            rp.create_at AS createAt,
            rp.update_at AS updateAt
        FROM sys_role_permission rp
        -- 关联角色表（必关联，角色权限关联依赖角色）
        INNER JOIN sys_role r ON rp.role_id = r.id
        -- 关联权限表（必关联，角色权限关联依赖权限）
        INNER JOIN sys_permission p ON rp.perm_id = p.id
        -- 动态筛选条件（来自QueryWrapper）
        ${ew.customSqlSegment}
        -- 排序：按更新时间倒序，最新关联优先
        ORDER BY rp.update_at DESC
    """)
    IPage<CommonSysRolePermissionDTO> queryPage(
            Page<CommonSysRolePermissionDTO> page,
            @Param(Constants.WRAPPER) Wrapper<SysRolePermission> wrapper
    );
}