package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysUserDTO;
import com.ru.app.common.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("SELECT * FROM sys_user WHERE user_account = #{userAccount}")
    SysUser selectByUserAccount(@Param("userAccount") String userAccount);
    @Select("SELECT " +
            "u.id AS id, " +
            "u.user_account AS userAccount, " +
            "u.user_password AS userPassword,"+
            "u.real_name AS realName, " +
            "u.gender AS gender, " +
            "u.phone AS phone, " +
            "u.email AS email, " +
            "u.user_type AS userType,"+
            "u.dept_id AS deptId, "+
            "d2.dept_name AS deptName, "+
            "u.class_id AS classId, "+
            "c.class_name AS className, "+
            "u.user_status AS userStatus, " +
            "u.remark AS remark,"+
            "u.create_at AS createAt,"+
            "u.update_at AS updateAt "+
            "FROM sys_user u " +
            "LEFT JOIN base_class c ON u.class_id = c.id "+
            "LEFT JOIN base_major m ON c.major_id = m.id "+
            "LEFT JOIN base_dept d1 ON m.dept_id = d1.id "+
            "LEFT JOIN base_dept d2 ON u.dept_id = d2.id "+
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY u.update_at DESC, u.create_at DESC" +
            "")        // 注入动态条件（WHERE部分）
    IPage<CommonSysUserDTO> queryPage(
            Page<CommonSysUserDTO> page,          // 分页参数
            @Param("ew") Wrapper<SysUser> wrapper  // 条件构造器
    );
    @Select("SELECT " +
            "u.id AS id, " +
            "u.user_account AS userAccount, " +
            "u.user_password AS userPassword,"+
            "u.real_name AS realName, " +
            "u.gender AS gender, " +
            "u.phone AS phone, " +
            "u.email AS email, " +
            "u.user_type AS userType,"+
            "u.dept_id AS deptId, "+
            "d2.dept_name AS deptName, "+
            "u.class_id AS classId, "+
            "c.class_name AS className, "+
            "u.user_status AS userStatus, " +
            "u.remark AS remark,"+
            "u.create_at AS createAt,"+
            "u.update_at AS updateAt "+
            "FROM sys_user u " +
            "LEFT JOIN base_class c ON u.class_id = c.id "+
            "LEFT JOIN base_major m ON c.major_id = m.id "+
            "LEFT JOIN base_dept d1 ON m.dept_id = d1.id "+
            "LEFT JOIN base_dept d2 ON u.dept_id = d2.id "+
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY u.update_at DESC, u.create_at DESC" +
            "")        // 注入动态条件（WHERE部分）        // 注入动态条件（WHERE部分）
    List<CommonSysUserDTO> queryAll(@Param("ew") Wrapper<SysUser> wrapper);
}
