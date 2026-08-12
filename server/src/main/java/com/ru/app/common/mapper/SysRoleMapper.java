package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysRoleDTO;
import com.ru.app.common.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface SysRoleMapper extends BaseMapper<SysRole> {
    @Select("SELECT " +
            "r.id, " +
            "r.role_group AS roleGroup, "+
            "r.role_code AS roleCode, "+
            "r.role_name AS roleName, "+
            "r.create_at AS createAt, " +
            "r.update_at AS updateAt, " +
            "r.remark " +
            "FROM sys_role r " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY r.update_at DESC, r.create_at DESC" +
            "")
    IPage<CommonSysRoleDTO> queryPage(
            Page<CommonSysRoleDTO> page,
            @Param("ew") Wrapper<SysRole> wrapper
    );
    @Select("SELECT " +
            "r.id, " +
            "r.role_group AS roleGroup, "+
            "r.role_code AS roleCode, "+
            "r.role_name AS roleName, "+
            "r.create_at AS createAt, " +
            "r.update_at AS updateAt, " +
            "r.remark " +
            "FROM sys_role r " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY r.update_at DESC, r.create_at DESC" +
            "")
    List<CommonSysRoleDTO> queryAll(
            @Param("ew") Wrapper<SysRole> wrapper
    );
}
