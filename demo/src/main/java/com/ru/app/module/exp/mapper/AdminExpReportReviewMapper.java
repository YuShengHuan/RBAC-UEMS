package com.ru.app.module.exp.mapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.module.exp.dto.FrontReportReviewStatusDTO;
import com.ru.app.common.mapper.ExpReportReviewMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminExpReportReviewMapper extends ExpReportReviewMapper {
    @Select(
            """
            SELECT
                ep.id AS projectId,
                er.id AS reportId,
                erw.id AS reviewId,
                ec.course_name AS courseName,
                ep.project_name AS projectName,
                u1.user_account AS reviewUserAccount,
                u1.real_name AS reviewRealName,
                u1.id AS reviewUserId,
                c.class_name AS className,
                u2.id AS uploadUserId,
                u2.user_account AS uploadUserAccount,
                u2.real_name AS uploadRealName
            FROM
                exp_project ep
            JOIN
                exp_teaching_core etc ON ep.teaching_core_id = etc.id
            JOIN
                exp_course ec ON etc.course_id = ec.id
            JOIN
                sys_user u1 ON etc.user_id = u1.id
            JOIN
                base_class c ON etc.class_id = c.id
            JOIN
                exp_report er ON ep.id = er.project_id AND er.attachment_type = 2
            JOIN
                sys_user u2 ON c.id = u2.class_id AND u2.id = er.upload_user_id AND u2.user_type = 3
            LEFT JOIN
                exp_report_review erw  on er.id=erw.report_id
            ${ew.customSqlSegment}
            """
    )
    IPage<FrontReportReviewStatusDTO> queryReportReviewStatus(
            Page<FrontReportReviewStatusDTO> page,
            @Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
