package com.ru.app.module.authority.mapper;


import com.ru.app.common.mapper.SysRolePermissionMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminSysRolePermissionMapper extends SysRolePermissionMapper {
    @Delete(
            """
                    DELETE FROM sys_role_permission 
                    WHERE role_id=#{roleId}
                    """
    )
    boolean deleteByRoleId(@Param("roleId") Long roleId);
}