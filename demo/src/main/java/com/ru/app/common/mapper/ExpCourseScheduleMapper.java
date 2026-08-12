package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonExpCourseScheduleDTO;
import com.ru.app.common.entity.ExpCourseSchedule;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ExpCourseScheduleMapper extends BaseMapper<ExpCourseSchedule> {
    @Select("SELECT " +
            "ecs.id, " +
            "etc.semester, " +
            "etc.id AS teachingCoreId, " +
            "ec.course_name AS courseName, " +// 关联课程名称（对应DTO）
            "c.class_name AS className, " +  // 关联班级名称（对应DTO）
            "u.user_account AS userAccount, " +    // 关联教师工号
            "u.real_name AS realName, " +    // 关联教师姓名（对应DTO）
            "b.id AS labId, " +              // 关联实验室ID（对应DTO）
            "b.lab_location AS labLocation, " +// 关联实验室位置（对应DTO）
            "ecs.week_start AS weekStart, " +
            "ecs.week_end AS weekEnd, " +
            "ecs.week_type AS weekType, " +
            "ecs.week_custom AS weekCustom, " +
            "ecs.week_day AS weekDay, " +
            "ecs.period_start AS periodStart, " +
            "ecs.period_end AS periodEnd, " +
            "ecs.class_hours AS classHours, " +
            "ecs.is_report AS isReport, " +
            "ecs.create_at AS createAt, " +
            "ecs.update_at AS updateAt " +
            "FROM exp_course_schedule ecs " +
            "LEFT JOIN exp_teaching_core etc ON ecs.teaching_core_id = etc.id " +
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id " +
            "LEFT JOIN base_class c ON etc.class_id = c.id " +
            "LEFT JOIN sys_user u ON etc.user_id = u.id " +
            "LEFT JOIN exp_lab b ON ecs.lab_id = b.id " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY ecs.update_at DESC, ecs.create_at DESC" +
            "")                              // 动态条件仍生效（基于实体字段）
    IPage<CommonExpCourseScheduleDTO> queryPage(
            Page<CommonExpCourseScheduleDTO> page,
            @Param(Constants.WRAPPER) Wrapper<ExpCourseSchedule> wrapper
    );

    @Select("SELECT " +
            "ecs.id, " +
            "etc.semester, " +
            "etc.id AS teachingCoreId, " +
            "ec.course_name AS courseName, " +// 关联课程名称（对应DTO）
            "c.class_name AS className, " +  // 关联班级名称（对应DTO）
            "u.user_account AS userAccount, " +    // 关联教师工号
            "u.real_name AS realName, " +    // 关联教师姓名（对应DTO）
            "b.id AS labId, " +              // 关联实验室ID（对应DTO）
            "b.lab_location AS labLocation, " +// 关联实验室位置（对应DTO）
            "ecs.week_start AS weekStart, " +
            "ecs.week_end AS weekEnd, " +
            "ecs.week_type AS weekType, " +
            "ecs.week_custom AS weekCustom, " +
            "ecs.week_day AS weekDay, " +
            "ecs.period_start AS periodStart, " +
            "ecs.period_end AS periodEnd, " +
            "ecs.class_hours AS classHours, " +
            "ecs.is_report AS isReport, " +
            "ecs.create_at AS createAt, " +
            "ecs.update_at AS updateAt " +
            "FROM exp_course_schedule ecs " +
            "JOIN exp_teaching_core etc ON ecs.teaching_core_id = etc.id " +
            "JOIN exp_course ec ON etc.course_id = ec.id " +
            "JOIN base_class c ON etc.class_id = c.id " +
            "JOIN sys_user u ON etc.user_id = u.id " +
            "JOIN exp_lab b ON ecs.lab_id = b.id " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY ecs.update_at DESC, ecs.create_at DESC" +
            "")                               // 动态条件仍生效（基于实体字段）
    List<CommonExpCourseScheduleDTO> queryAll(@Param(Constants.WRAPPER) Wrapper<ExpCourseSchedule> wrapper);
    @Select("SELECT " +
            "ecs.id, " +
            "etc.semester, " +
            "etc.id AS teachingCoreId, " +
            "ec.course_name AS courseName, " +// 关联课程名称（对应DTO）
            "c.class_name AS className, " +  // 关联班级名称（对应DTO）
            "u.user_account AS userAccount, " +    // 关联教师工号
            "u.real_name AS realName, " +    // 关联教师姓名（对应DTO）
            "b.id AS labId, " +              // 关联实验室ID（对应DTO）
            "b.lab_location AS labLocation, " +// 关联实验室位置（对应DTO）
            "ecs.week_start AS weekStart, " +
            "ecs.week_end AS weekEnd, " +
            "ecs.week_type AS weekType, " +
            "ecs.week_custom AS weekCustom, " +
            "ecs.week_day AS weekDay, " +
            "ecs.period_start AS periodStart, " +
            "ecs.period_end AS periodEnd, " +
            "ecs.class_hours AS classHours, " +
            "ecs.is_report AS isReport, " +
            "ecs.create_at AS createAt, " +
            "ecs.update_at AS updateAt " +
            "FROM exp_course_schedule ecs " +
            "JOIN exp_teaching_core etc ON ecs.teaching_core_id = etc.id " +
            "JOIN exp_course ec ON etc.course_id = ec.id " +
            "JOIN base_class c ON etc.class_id = c.id " +
            "JOIN sys_user u ON etc.user_id = u.id " +
            "JOIN exp_lab b ON ecs.lab_id = b.id " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY ecs.update_at DESC, ecs.create_at DESC" +
            "")                               // 动态条件仍生效（基于实体字段）
    CommonExpCourseScheduleDTO queryDetail(@Param(Constants.WRAPPER) Wrapper<ExpCourseSchedule> wrapper);
}
