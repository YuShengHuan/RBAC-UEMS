package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonExpCourseDTO;
import com.ru.app.common.entity.ExpCourse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ExpCourseMapper extends BaseMapper<ExpCourse> {
    @Select("SELECT " +
            "ec.id, " +
            "ec.course_code AS courseCode, " + // 对应DTO的
            "ec.course_name AS courseName, "+   // 对应DTO的
            "ec.course_type AS courseType, "+   // 对应DTO
            "d.id AS deptId, " +                 // 关联学院表的名称，映射DTO
            "d.dept_name AS deptName, " +                 // 关联学院表的名称，映射DTO
            "ec.create_at AS createAt, "+
            "ec.update_at AS updateAt " +
            "FROM exp_course ec " +                          // 主表
            "LEFT JOIN base_dept d ON ec.dept_id = d.id "+    // 关联学院表（别名d），通过departmentId关联
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY ec.update_at DESC, ec.create_at DESC" +
            "")                                                          // 注入动态条件（来自Wrapper）
    IPage<CommonExpCourseDTO> queryPage(
            Page<CommonExpCourseDTO> page,
            @Param(Constants.WRAPPER) Wrapper<ExpCourse> wrapper
    );

    @Select("SELECT " +
            "ec.id, " +
            "ec.course_code AS courseCode, " + // 对应DTO的
            "ec.course_name AS courseName, "+   // 对应DTO的
            "ec.course_type AS courseType, "+   // 对应DTO
            "d.id AS deptId, " +                 // 关联学院表的名称，映射DTO
            "d.dept_name AS deptName, " +                 // 关联学院表的名称，映射DTO
            "ec.create_at AS createAt, "+
            "ec.update_at AS updateAt " +
            "FROM exp_course ec " +                          // 主表
            "LEFT JOIN base_dept d ON ec.dept_id = d.id "+
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY ec.update_at DESC, ec.create_at DESC" +
            "")
    List<CommonExpCourseDTO> queryAll(@Param(Constants.WRAPPER) Wrapper<ExpCourse> wrapper);
}
