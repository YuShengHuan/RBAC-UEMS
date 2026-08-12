package com.ru.app.common.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonExpProjectDTO;
import com.ru.app.common.entity.ExpProject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ExpProjectMapper extends BaseMapper<ExpProject> {
    @Select("SELECT " +
            "ep.id, " +
            "ep.project_code AS projectCode, " +
            "ep.project_name AS projectName, " +
            "etc.semester, " +
            "ep.teaching_core_id AS teachingCoreId, " +
            "u.user_account AS userAccount, " +    // 关联教师工号
            "u.real_name AS realName, " +
            "ec.course_name AS courseName, " +
            "c.class_name AS className, " +
            "ep.project_week_start AS projectWeekStart, " +
            "ep.project_week_end AS projectWeekEnd, " +
            "ep.weekly_hours AS weeklyHours, " +
            "ep.plan_hours AS planHours, " +
            "ep.actual_hours AS actualHours, " +
            "ep.exp_category AS expCategory, " +
            "ep.exp_type AS expType, " +
            "ep.subject AS subject, " +
            "ep.group_num AS groupNum, " +
            "ep.exp_person_type AS expPersonType, " +
            "ep.exp_requirement AS expRequirement, " +
            "ep.remark AS remark, " +
            "ep.create_at AS createAt, " +
            "ep.update_at AS updateAt " +
            "FROM exp_project ep " +
            "LEFT JOIN exp_teaching_core etc ON ep.teaching_core_id = etc.id " +
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id " +
            "LEFT JOIN base_class c ON etc.class_id = c.id " +
            "LEFT JOIN sys_user u ON etc.user_id = u.id " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY ep.update_at DESC, ep.create_at DESC" +
            "")
    IPage<CommonExpProjectDTO> queryPage(
            Page<CommonExpProjectDTO> page,
            @Param(Constants.WRAPPER) Wrapper<ExpProject> wrapper
    );

    @Select("SELECT " +
            "ep.id, " +
            "ep.project_code AS projectCode, " +
            "ep.project_name AS projectName, " +
            "etc.semester, " +
            "ep.teaching_core_id AS teachingCoreId, " +
            "u.user_account AS userAccount, " +    // 关联教师工号
            "u.real_name AS realName, " +
            "ec.course_name AS courseName, " +
            "c.class_name AS className, " +
            "ep.project_week_start AS projectWeekStart, " +
            "ep.project_week_end AS projectWeekEnd, " +
            "ep.weekly_hours AS weeklyHours, " +
            "ep.plan_hours AS planHours, " +
            "ep.actual_hours AS actualHours, " +
            "ep.exp_category AS expCategory, " +
            "ep.exp_type AS expType, " +
            "ep.subject AS subject, " +
            "ep.group_num AS groupNum, " +
            "ep.exp_person_type AS expPersonType, " +
            "ep.exp_requirement AS expRequirement, " +
            "ep.remark AS remark, " +
            "ep.create_at AS createAt, " +
            "ep.update_at AS updateAt " +
            "FROM exp_project ep " +
            "LEFT JOIN exp_teaching_core etc ON ep.teaching_core_id = etc.id " +
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id " +
            "LEFT JOIN base_class c ON etc.class_id = c.id " +
            "LEFT JOIN sys_user u ON etc.user_id = u.id " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY ep.update_at DESC, ep.create_at DESC" +
            "")
    List<CommonExpProjectDTO> queryAll(@Param(Constants.WRAPPER) Wrapper<ExpProject> wrapper);
    @Select("SELECT " +
            "ep.id, " +
            "ep.project_code AS projectCode, " +
            "ep.project_name AS projectName, " +
            "etc.semester, " +
            "ep.teaching_core_id AS teachingCoreId, " +
            "u.user_account AS userAccount, " +    // 关联教师工号
            "u.real_name AS realName, " +
            "ec.course_name AS courseName, " +
            "c.class_name AS className, " +
            "ep.project_week_start AS projectWeekStart, " +
            "ep.project_week_end AS projectWeekEnd, " +
            "ep.weekly_hours AS weeklyHours, " +
            "ep.plan_hours AS planHours, " +
            "ep.actual_hours AS actualHours, " +
            "ep.exp_category AS expCategory, " +
            "ep.exp_type AS expType, " +
            "ep.subject AS subject, " +
            "ep.group_num AS groupNum, " +
            "ep.exp_person_type AS expPersonType, " +
            "ep.exp_requirement AS expRequirement, " +
            "ep.remark AS remark, " +
            "ep.create_at AS createAt, " +
            "ep.update_at AS updateAt " +
            "FROM exp_project ep " +
            "LEFT JOIN exp_teaching_core etc ON ep.teaching_core_id = etc.id " +
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id " +
            "LEFT JOIN base_class c ON etc.class_id = c.id " +
            "LEFT JOIN sys_user u ON etc.user_id = u.id " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY ep.update_at DESC, ep.create_at DESC" +
            "")
    CommonExpProjectDTO queryDetail(@Param(Constants.WRAPPER) Wrapper<ExpProject> wrapper);
}