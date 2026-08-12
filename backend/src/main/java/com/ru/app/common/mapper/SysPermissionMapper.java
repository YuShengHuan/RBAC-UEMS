package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysPermissionDTO;
import com.ru.app.common.entity.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    // 主查询：分页查询权限，同时关联子权限
    @Select("SELECT " +
            "p.id, " +
            "p.parent_code AS parentCode, "+
            "p.perm_group AS permGroup, "+
            "p.perm_code AS permCode, " +
            "p.perm_name AS permName, " +
            "p.perm_type AS permType, " +
            "p.request_uri AS requestUri, " +
            "p.perm_status AS permStatus, " +
            "p.create_at AS createAt, " +
            "p.update_at AS updateAt, " +
            "p.remark " +
            "FROM sys_permission p " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY p.update_at DESC, p.create_at DESC" +
            "")
    IPage<CommonSysPermissionDTO> queryPage(
            Page<CommonSysPermissionDTO> page,
            @Param(Constants.WRAPPER) Wrapper<SysPermission> wrapper
    );
    @Select("SELECT " +
            "p.id, " +
            "p.parent_code AS parentCode, "+
            "p.perm_group AS permGroup, "+
            "p.perm_code AS permCode, " +
            "p.perm_name AS permName, " +
            "p.perm_type AS permType, " +
            "p.request_uri AS requestUri, " +
            "p.perm_status AS permStatus, " +
            "p.create_at AS createAt, " +
            "p.update_at AS updateAt, " +
            "p.remark " +
            "FROM sys_permission p "+
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY p.update_at DESC, p.create_at DESC" +
            "")
    List<CommonSysPermissionDTO> queryAll(
            @Param(Constants.WRAPPER) Wrapper<SysPermission> wrapper
    );
}
