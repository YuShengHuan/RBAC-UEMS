package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysUserRoleDTO;
import com.ru.app.common.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    @Select("""
        SELECT 
            ur.id,
            ur.user_id AS userId,
            u.user_account AS userAccount,
            u.real_name AS realName,  
            ur.role_id AS roleId,
            r.role_name AS roleName,       
            r.role_code AS roleCode,
            d.id AS deptId,
            d.dept_name AS deptName,
            ur.create_at AS createAt,
            ur.update_at AS updateAt
        FROM sys_user_role ur
        INNER JOIN sys_user u ON ur.user_id = u.id
        INNER JOIN sys_role r ON ur.role_id = r.id
        LEFT JOIN base_dept d ON ur.dept_id = d.id
        ${ew.customSqlSegment}
        ORDER BY ur.update_at DESC
    """)
    IPage<CommonSysUserRoleDTO> queryPage(
            Page<CommonSysUserRoleDTO> page,
            @Param(Constants.WRAPPER) Wrapper<SysUserRole> wrapper
    );
    @Select("""
        SELECT * FROM sys_user_role ur
        JOIN sys_user u ON u.id = ur.user_id AND u.user_account = #{userAccount}
        JOIN sys_role r ON r.id = ur.role_id AND r.role_code = #{roleCode}    
    """)
    SysUserRole selectUserRoleByUserAccountAndRoleCode(
            @Param("userAccount") String userAccount,  // 改为String类型（匹配数据库user_account字段类型）
            @Param("roleCode") String roleCode         // 改为String类型（匹配数据库role_code字段类型）
    );
}