package com.ru.app.module.authority.mapper;


import com.ru.app.common.entity.SysPermission;
import com.ru.app.common.mapper.SysPermissionMapper;
import com.ru.app.module.authority.dto.SysPermissionWithOwnDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminSysPermissionMapper extends SysPermissionMapper {
    /**
     * 左连接查询：全量权限+指定角色的拥有标记（isOwn）
     * @param roleId 角色ID（可为null，null时所有权限isOwn=0）
     */
    @Select("""
            SELECT 
                p.id, 
                p.parent_code AS parentCode,
                p.perm_group AS permGroup,
                p.perm_code AS permCode, 
                p.perm_name AS permName, 
                p.perm_type AS permType, 
                p.request_uri AS requestUri, 
                p.perm_status AS permStatus, 
                p.create_at AS createAt,
                p.update_at AS updateAt,
                p.remark,
                CASE WHEN rp.perm_id IS NOT NULL THEN 1 ELSE 0 END AS is_own  -- 关联到角色则为1，否则0
            FROM sys_permission p
            LEFT JOIN sys_role_permission rp 
                ON p.id = rp.perm_id 
                AND rp.role_id = #{roleId}  -- 角色ID条件放ON里，保证左连接保留全量权限
            WHERE p.perm_status=1
            ORDER BY p.parent_code ASC, p.perm_code ASC
            """)
    List<SysPermissionWithOwnDTO> selectAllPermissionsWithOwnFlag(@Param("roleId") Long roleId);


    @Select("""
            SELECT 
                p.id, 
                p.parent_code AS parentCode,
                p.perm_group AS permGroup,
                p.perm_code AS permCode, 
                p.perm_name AS permName, 
                p.perm_type AS permType, 
                p.request_uri AS requestUri, 
                p.perm_status AS permStatus, 
                p.create_at AS createAt,
                p.update_at AS updateAt,
                p.remark
            FROM sys_permission p
            JOIN sys_role_permission rp ON p.id = rp.perm_id 
            JOIN sys_role r ON rp.role_id=r.id AND r.role_code=#{roleCode}
            """)
    List<SysPermission> selectAllPermissionByRoleCode(@Param("roleCode") String roleCode);
}
