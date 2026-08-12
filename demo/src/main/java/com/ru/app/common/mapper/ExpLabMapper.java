package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonExpLabDTO;
import com.ru.app.common.entity.ExpLab;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ExpLabMapper extends BaseMapper<ExpLab> {
    @Select("SELECT " +
            "b.id, " +
            "b.lab_code AS labCode, " + // 对应DTO的
            "b.lab_name AS labName, "+   // 对应DTO的
            "b.lab_location AS labLocation, "+   // 对应DTO
            "d.id AS deptId, " +                 // 关联学院表的名称，映射DTO
            "d.dept_name AS deptName, " +                 // 关联学院表的名称，映射DTO
            "b.create_at AS createAt, "+
            "b.update_at AS updateAt " +
            "FROM exp_lab b " +                          // 主表
            "LEFT JOIN base_dept d ON b.dept_id = d.id "+    // 关联学院表（别名d），通过departmentId关联
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY b.update_at DESC, b.create_at DESC" +
            "")                                                       // 注入动态条件（来自Wrapper）
    IPage<CommonExpLabDTO> queryPage(
            Page<CommonExpLabDTO> page,
            @Param(Constants.WRAPPER) Wrapper<ExpLab> wrapper
    );

    @Select("SELECT " +
            "b.id, " +
            "b.lab_code AS labCode, " + // 对应DTO的
            "b.lab_name AS labName, "+   // 对应DTO的
            "b.lab_location AS labLocation, "+   // 对应DTO
            "d.id AS deptId, " +                 // 关联学院表的名称，映射DTO
            "d.dept_name AS deptName, " +                 // 关联学院表的名称，映射DTO
            "b.create_at AS createAt, "+
            "b.update_at AS updateAt " +
            "FROM exp_lab b " +                          // 主表
            "LEFT JOIN base_dept d ON b.dept_id = d.id "+
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY b.update_at DESC, b.create_at DESC" +
            "")                                // 注入动态条件（来自Wrapper）
    List<CommonExpLabDTO> queryAll(@Param(Constants.WRAPPER) Wrapper<ExpLab> wrapper);

}
