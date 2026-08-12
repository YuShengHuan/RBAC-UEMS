package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonBaseClassDTO;
import com.ru.app.common.entity.BaseClass;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface BaseClassMapper extends BaseMapper<BaseClass> {
    @Select("SELECT " +
            "c.id, " +
            "c.class_code AS classCode, " + // 对应DTO的
            "c.class_name AS className, "+   // 对应DTO的
            "m.id AS majorId, " +                 // 关联表的id
            "m.major_name AS majorName, " +                 // 关联表的名称，映射DTO
            "d.id AS deptId, " +
            "d.dept_name AS deptName, " +                 // 关联学院表的名称，映射DTO
            "c.grade AS grade, "+   // 对应DTO的
            "c.student_count AS studentCount, "+   // 对应DTO的
            "c.create_at AS createAt, "+
            "c.update_at AS updateAt " +
            "FROM base_class c " +                          // 主表
            "LEFT JOIN base_major m ON c.major_id = m.id "+    // 关联专业表（别名d），通过departmentId关联
            "LEFT JOIN base_dept d ON m.dept_id = d.id "+    // 关联专业表（别名d），通过departmentId关联
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY c.update_at DESC, c.create_at DESC" +
            "")                               // 注入动态条件（来自Wrapper）
    IPage<CommonBaseClassDTO> queryPage(
            Page<CommonBaseClassDTO> page,
            @Param(Constants.WRAPPER) Wrapper<BaseClass> wrapper
    );
    @Select("SELECT " +
            "c.id, " +
            "c.class_code AS classCode, " + // 对应DTO的
            "c.class_name AS className, "+   // 对应DTO的
            "m.id AS majorId, " +                 // 关联表的id
            "m.major_name AS majorName, " +                 // 关联表的名称，映射DTO
            "d.id AS deptId, " +
            "d.dept_name AS deptName, " +                 // 关联学院表的名称，映射DTO
            "c.grade AS grade, "+   // 对应DTO的
            "c.student_count AS studentCount, "+   // 对应DTO的
            "c.create_at AS createAt, "+
            "c.update_at AS updateAt " +
            "FROM base_class c " +                          // 主表
            "LEFT JOIN base_major m ON c.major_id = m.id "+    // 关联专业表（别名d），通过departmentId关联
            "LEFT JOIN base_dept d ON m.dept_id = d.id "+
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY c.update_at DESC, c.create_at DESC" +
            "")                                      // 注入动态条件（来自Wrapper）
    List<CommonBaseClassDTO> queryAll(@Param(Constants.WRAPPER) Wrapper<BaseClass> wrapper);
}
