package com.ru.app.common.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonExpReportReviewDTO;
import com.ru.app.common.entity.ExpReportReview;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ExpReportReviewMapper extends BaseMapper<ExpReportReview> {
    @Select("SELECT " +
            "erw.id, " +
            "etc.semester, " +
            "ec.course_name AS courseName, "+
            "ep.project_name AS projectName, " +
            "c.class_name AS className, " +
            "u1.user_account AS uploadUserAccount, " +
            "u1.real_name AS uploadRealName, " +
            "u1.user_type AS uploadUserType, " +
            "er.id AS reportId, " +
            "u2.id AS reviewUserId, " +
            "u2.user_account AS reviewUserAccount, " +
            "u2.real_name AS reviewRealName, " +
            "u2.user_type AS reviewUserType, " +
            "erw.score, " +
            "erw.review_comment AS reviewComment, " +
            "erw.plagiarism_rate AS plagiarismRate, " +
            "er.remark AS remark, "+
            "erw.create_at AS createAt, "+
            "erw.update_at AS updateAt " +
            "FROM exp_report_review erw " +// 主表
            "LEFT JOIN exp_report er ON erw.report_id = er.id "+
            "LEFT JOIN exp_project ep ON er.project_id = ep.id "+
            "LEFT JOIN exp_teaching_core etc ON ep.teaching_core_id = etc.id "+    // 关联课程辅助表
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id "+
            "LEFT JOIN base_class c ON etc.class_id = c.id "+    // 关联班级表
            "LEFT JOIN sys_user u1 ON er.upload_user_id = u1.id "+    // 关联用户表
            "LEFT JOIN sys_user u2 ON erw.review_user_id = u2.id "+    // 关联用户表
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY erw.update_at DESC, erw.create_at DESC" +
            "")                                                        // 注入动态条件（来自Wrapper）
    IPage<CommonExpReportReviewDTO> queryPage(
            Page<CommonExpReportReviewDTO> page,
            @Param(Constants.WRAPPER) Wrapper<ExpReportReview> wrapper
    );

    @Select("SELECT " +
            "erw.id, " +
            "etc.semester, " +
            "ec.course_name AS courseName, "+
            "ep.project_name AS projectName, " +
            "c.class_name AS className, " +
            "u1.user_account AS uploadUserAccount, " +
            "u1.real_name AS uploadRealName, " +
            "u1.user_type AS uploadUserType, " +
            "er.id AS reportId, " +
            "u2.id AS reviewUserId, " +
            "u2.user_account AS reviewUserAccount, " +
            "u2.real_name AS reviewRealName, " +
            "u2.user_type AS reviewUserType, " +
            "erw.score, " +
            "erw.review_comment AS reviewComment, " +
            "erw.plagiarism_rate AS plagiarismRate, " +
            "er.remark AS remark, "+
            "erw.create_at AS createAt, "+
            "erw.update_at AS updateAt " +
            "FROM exp_report_review erw " +// 主表
            "LEFT JOIN exp_report er ON erw.report_id = er.id "+
            "LEFT JOIN exp_project ep ON er.project_id = ep.id "+
            "LEFT JOIN exp_teaching_core etc ON ep.teaching_core_id = etc.id "+    // 关联课程辅助表
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id "+
            "LEFT JOIN base_class c ON etc.class_id = c.id "+    // 关联班级表
            "LEFT JOIN sys_user u1 ON er.upload_user_id = u1.id "+    // 关联用户表
            "LEFT JOIN sys_user u2 ON erw.review_user_id = u2.id "+    // 关联用户表
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY erw.update_at DESC, erw.create_at DESC" +
            "")
    CommonExpReportReviewDTO queryDetail(@Param(Constants.WRAPPER) Wrapper<ExpReportReview> wrapper);
}
