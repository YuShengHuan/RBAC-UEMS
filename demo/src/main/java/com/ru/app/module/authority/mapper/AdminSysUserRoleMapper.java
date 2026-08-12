package com.ru.app.module.authority.mapper;


import com.ru.app.common.entity.SysRole;
import com.ru.app.common.mapper.SysUserRoleMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminSysUserRoleMapper extends SysUserRoleMapper {
    @Select("""
        SELECT 
            r.role_name AS roleName,       
            r.role_code AS roleCode
        FROM sys_user_role ur
        INNER JOIN sys_user u ON ur.user_id = u.id
        INNER JOIN sys_role r ON ur.role_id = r.id
        WHERE ur.user_id=#{userId}
        ORDER BY ur.update_at DESC
    """)
    List<SysRole> findAllRoleByUserId(@Param("userId") Long userId);
}