package com.ru.app.module.user.mapper;

import com.ru.app.common.dto.SelfRolePermissionDTO;
import com.ru.app.common.mapper.SysUserMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface AdminSysUserMapper extends SysUserMapper {
    @Select("""
    SELECT
        r.role_code AS roleCode,
        r.role_name AS roleName,
        r.id AS roleId
    FROM
        sys_user_role ur
    INNER JOIN sys_role r ON ur.role_id = r.id  -- 只关联存在的角色
    WHERE
        ur.user_id = #{userId} 
    GROUP BY
        r.role_code, r.role_name, r.id  -- 分组字段与查询字段一致
    ORDER BY
        r.id
""")
    @Results({
            // 角色唯一标识（用roleId更可靠，roleKey可能重复）
            @Result(id = true, column = "roleId", property = "roleId"),
            @Result(column = "roleCode", property = "roleCode"),  // 修正：用roleId作为主键（唯一）
            @Result(column = "roleName", property = "roleName"),
            // 权限列表：通过子查询获取，传递roleId（更唯一）给子查询
            @Result(
                    property = "permCodes",
                    column = "roleId",  // 改用roleId传递参数（比roleKey更可靠）
                    many = @org.apache.ibatis.annotations.Many(
                            select = "com.ru.app.module.user.mapper.AdminSysUserMapper.getPermissionKeysByRoleId"
                    )
            )
    })
    List<SelfRolePermissionDTO> getUserRolePermissions(@Param("userId") Long userId);
    /**
     * 子查询：根据角色ID获取权限列表（过滤null，确保返回纯Integer列表）
     * @param roleId 角色ID（唯一标识，替代roleKey）
     * @return 权限标识列表（无权限时返回空列表[]）
     */
    @Select("""
    SELECT
        p.perm_code AS permCode 
    FROM
        sys_role_permission rp
    INNER JOIN sys_permission p ON rp.perm_id = p.id 
        AND p.perm_code IS NOT NULL  -- 过滤权限值为null的记录
    WHERE
        rp.role_id = #{roleId}  -- 用roleId关联（唯一可靠）
    ORDER BY
        p.id
""")
    List<String> getPermissionKeysByRoleId(@Param("roleId") Long roleId);  // 参数类型改为Long（与roleId一致）
}
