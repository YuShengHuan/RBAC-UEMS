package com.ru.app.module.dict.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysDictDTO;
import com.ru.app.common.entity.SysDict;
import com.ru.app.common.mapper.SysDictMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface AdminSysDictMapper extends SysDictMapper {

    @Select("SELECT " +
            "dt.id, " +
            "dt.dict_group AS dictGroup, " +  // 映射DTO的驼峰字段
            "dt.dict_key AS dictKey, " +
            "dt.dict_value AS dictValue, " +
            "dt.remark AS remark, " +
            "dt.create_at AS createAt, " +
            "dt.update_at AS updateAt " +
            "FROM sys_dict dt " +
            "${ew.customSqlSegment}")  // 注入动态条件（来自Wrapper）
    IPage<CommonSysDictDTO> queryPage(
            Page<CommonSysDictDTO> page,
            @Param(Constants.WRAPPER) Wrapper<SysDict> wrapper
    );
    @Select("SELECT " +
            "dt.id, " +
            "dt.dict_group AS dictGroup, " +  // 映射DTO的驼峰字段
            "dt.dict_key AS dictKey, " +
            "dt.dict_value AS dictValue, " +
            "dt.remark AS remark, " +
            "dt.create_at AS createAt, " +
            "dt.update_at AS updateAt " +
            "FROM sys_dict dt " +
            "${ew.customSqlSegment}")  // 注入动态条件（来自Wrapper）
    List<CommonSysDictDTO> queryAll(@Param(Constants.WRAPPER) Wrapper<SysDict> wrapper);
}