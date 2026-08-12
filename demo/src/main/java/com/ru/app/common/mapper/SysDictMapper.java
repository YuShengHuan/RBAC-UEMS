package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysDictDTO;
import com.ru.app.common.entity.SysDict;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface SysDictMapper extends BaseMapper<SysDict> {
    @Select("SELECT " +
            "dt.id, " +
            "dt.dict_group AS dictGroup, " +  // 映射DTO的驼峰字段
            "dt.dict_key AS dictKey, " +
            "dt.dict_value AS dictValue, " +
            "dt.remark AS remark, " +
            "dt.create_at AS createAt, " +
            "dt.update_at AS updateAt " +
            "FROM sys_dict dt " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY dt.update_at DESC, dt.create_at DESC" +
            "")                          // 注入动态条件（来自Wrapper）
    IPage<CommonSysDictDTO> queryPage(
            Page<CommonSysDictDTO> page,
            @Param(Constants.WRAPPER) Wrapper<SysDict> wrapper
    );
}