package com.ru.app.common.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonBaseMajorDTO;
import com.ru.app.common.entity.BaseMajor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BaseMajorMapper extends BaseMapper<BaseMajor> {
    @Select("SELECT " +
            "m.id, " +
            "m.major_code AS majorCode, " + // 对应DTO的
            "m.major_name AS majorName, "+   // 对应DTO的
            "d.id AS deptId, " +
            "d.dept_name AS deptName, " +                 // 关联学院表的名称，映射DTO
            "m.create_at AS createAt, "+
            "m.update_at AS updateAt " +
            "FROM base_major m " +                          // 主表
            "LEFT JOIN base_dept d ON m.dept_id = d.id "+    // 关联专业表（别名d），通过departmentId关联
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY m.update_at DESC, m.create_at DESC" +
            "")                                                        // 注入动态条件（来自Wrapper）
    IPage<CommonBaseMajorDTO> queryPage(
            Page<CommonBaseMajorDTO> page,
            @Param(Constants.WRAPPER) Wrapper<BaseMajor> wrapper
    );
    @Select("SELECT " +
            "m.id, " +
            "m.major_code AS majorCode, " + // 对应DTO的
            "m.major_name AS majorName, "+   // 对应DTO的
            "d.id AS deptId, " +
            "d.dept_name AS deptName, " +                 // 关联学院表的名称，映射DTO
            "m.create_at AS createAt, "+
            "m.update_at AS updateAt " +
            "FROM base_major m " +                          // 主表
            "LEFT JOIN base_dept d ON m.dept_id = d.id "+
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY m.update_at DESC, m.create_at DESC" +
            "")                                      // 注入动态条件（来自Wrapper）
    List<CommonBaseMajorDTO> queryAll(@Param(Constants.WRAPPER) Wrapper<BaseMajor> wrapper);
}
