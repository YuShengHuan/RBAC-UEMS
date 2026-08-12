package com.ru.app.module.authority.mapper;


import com.ru.app.common.mapper.SysRoleMapper;
import com.ru.app.module.authority.dto.SysPermissionWithOwnDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminSysRoleMapper extends SysRoleMapper {
    @Select("""
            SELECT 
                r.role_name
            FROM sys_role r
            WHERE r.role_code=#{roleCode}
            """)
    String selectRoleNameByRoleCode(@Param("roleCode") String roleCode);
}
