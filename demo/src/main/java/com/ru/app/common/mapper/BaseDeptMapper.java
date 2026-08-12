package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonBaseDeptDTO;
import com.ru.app.common.entity.BaseDept;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface BaseDeptMapper extends BaseMapper<BaseDept> {
    @Select("SELECT " +
            "d.id, " +
            "d.dept_code AS deptCode, " + // 对应DTO的
            "d.dept_name AS deptName, "+   // 对应DTO的
            "d.create_at AS createAt, "+
            "d.update_at AS updateAt " +
            "FROM base_dept d "+    // 主表
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY d.update_at DESC, d.create_at DESC" +
            "")                                                          // 注入动态条件（来自Wrapper）
    IPage<CommonBaseDeptDTO> queryPage(
            Page<CommonBaseDeptDTO> page,
            @Param(Constants.WRAPPER) Wrapper<BaseDept> wrapper
    );
    @Select("SELECT " +
            "d.id, " +
            "d.dept_code AS deptCode, " + // 对应DTO的
            "d.dept_name AS deptName, "+   // 对应DTO的
            "d.create_at AS createAt, "+
            "d.update_at AS updateAt " +
            "FROM base_dept d "+
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY d.update_at DESC, d.create_at DESC" +
            "")
    List<CommonBaseDeptDTO> queryAll(@Param(Constants.WRAPPER) Wrapper<BaseDept> wrapper);
}
