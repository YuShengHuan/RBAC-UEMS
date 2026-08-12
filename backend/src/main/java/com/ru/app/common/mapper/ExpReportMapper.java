package com.ru.app.common.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonExpReportDTO;
import com.ru.app.common.entity.ExpReport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ExpReportMapper extends BaseMapper<ExpReport> {
    @Select("SELECT " +
            "er.id, " +
            "etc.semester, " +
            "ec.course_name AS courseName, "+
            "ep.id AS projectId, " +
            "ep.project_name AS projectName, " +
            "c.class_name AS className, " +
            "u.id AS uploadUserId, " +
            "u.user_account AS uploadUserAccount, " +
            "u.real_name AS uploadRealName, " +
            "u.user_type AS uploadUserType, " +
            "er.file_path AS filePath, " +
            "er.attachment_type AS attachmentType, " +
            "er.remark AS remark, "+
            "er.create_at AS createAt, "+
            "er.update_at AS updateAt " +
            "FROM exp_report er " +// 主表
            "LEFT JOIN exp_project ep ON er.project_id = ep.id "+    // 关联课程表
            "LEFT JOIN exp_teaching_core etc ON ep.teaching_core_id = etc.id "+    // 关联课程辅助表
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id "+
            "LEFT JOIN base_class c ON etc.class_id = c.id "+    // 关联班级表
            "LEFT JOIN sys_user u ON er.upload_user_id = u.id "+    // 关联用户表
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY er.update_at DESC, er.create_at DESC" +
            "")                                // 注入动态条件（来自Wrapper）
    IPage<CommonExpReportDTO> queryPage(
            Page<CommonExpReportDTO> page,
            @Param(Constants.WRAPPER) Wrapper<ExpReport> wrapper
    );

    @Select("SELECT " +
            "er.id, " +
            "etc.semester, " +
            "ec.course_name AS courseName, "+
            "ep.id AS projectId, " +
            "ep.project_name AS projectName, " +
            "c.class_name AS className, " +
            "u.id AS uploadUserId, " +
            "u.user_account AS uploadUserAccount, " +
            "u.real_name AS uploadRealName, " +
            "u.user_type AS uploadUserType, " +
            "er.file_path AS filePath, " +
            "er.attachment_type AS attachmentType, " +
            "er.remark AS remark, "+
            "er.create_at AS createAt, "+
            "er.update_at AS updateAt " +
            "FROM exp_report er " +// 主表
            "LEFT JOIN exp_project ep ON er.project_id = ep.id "+    // 关联课程表
            "LEFT JOIN exp_teaching_core etc ON ep.teaching_core_id = etc.id "+    // 关联课程辅助表
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id "+
            "LEFT JOIN base_class c ON etc.class_id = c.id "+    // 关联班级表
            "LEFT JOIN sys_user u ON er.upload_user_id = u.id "+    // 关联用户表
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY er.update_at DESC, er.create_at DESC" +
            "")
    List<CommonExpReportDTO> queryAll(@Param(Constants.WRAPPER) Wrapper<ExpReport> wrapper);
    @Select("SELECT " +
            "er.id, " +
            "etc.semester, " +
            "ec.course_name AS courseName, "+
            "ep.id AS projectId, " +
            "ep.project_name AS projectName, " +
            "c.class_name AS className, " +
            "u.id AS uploadUserId, " +
            "u.user_account AS uploadUserAccount, " +
            "u.real_name AS uploadRealName, " +
            "u.user_type AS uploadUserType, " +
            "er.file_path AS filePath, " +
            "er.attachment_type AS attachmentType, " +
            "er.remark AS remark, "+
            "er.create_at AS createAt, "+
            "er.update_at AS updateAt " +
            "FROM exp_report er " +// 主表
            "LEFT JOIN exp_project ep ON er.project_id = ep.id "+    // 关联课程表
            "LEFT JOIN exp_teaching_core etc ON ep.teaching_core_id = etc.id "+    // 关联课程辅助表
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id "+
            "LEFT JOIN base_class c ON etc.class_id = c.id "+    // 关联班级表
            "LEFT JOIN sys_user u ON er.upload_user_id = u.id "+    // 关联用户表
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY er.update_at DESC, er.create_at DESC" +
            "")
    CommonExpReportDTO queryDetail(@Param(Constants.WRAPPER) Wrapper<ExpReport> wrapper);
}
