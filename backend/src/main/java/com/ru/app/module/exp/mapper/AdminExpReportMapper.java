package com.ru.app.module.exp.mapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.module.exp.dto.FrontReportSubmitStatusDTO;
import com.ru.app.common.dto.CommonExpReportDTO;
import com.ru.app.common.mapper.ExpReportMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AdminExpReportMapper extends ExpReportMapper {
    @Select(
            """
            SELECT
                ep.id AS projectId,
                er.id AS reportId,
                ec.course_name AS courseName,
                ep.project_name AS projectName,
                u1.user_account AS reviewUserAccount,
                u1.real_name AS reviewRealName,
                u1.id AS reviewUserId,
                c.class_name AS className,
                u2.id AS uploadUserId,
                u2.user_account AS uploadUserAccount,
                u2.real_name AS uploadRealName,
                er.file_path AS filePath
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
                sys_user u2 ON c.id = u2.class_id  AND u2.user_type = 3
            LEFT JOIN
                exp_report er ON ep.id = er.project_id AND u2.id = er.upload_user_id AND er.attachment_type = 2
            ${ew.customSqlSegment}
            """
    )
    IPage<FrontReportSubmitStatusDTO> queryReportStatus(
            Page<FrontReportSubmitStatusDTO> page,
            @Param(Constants.WRAPPER) Wrapper<?> wrapper);
    @Select("SELECT " +
            "er.id, " +
            "etc.semester, " +
            "ec.course_name AS courseName, "+
            "ep.id AS projectId, " +
            "ep.project_name AS projectName, " +
            "c.class_name AS className, " +
            "c.id AS classId, "+
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
            "LEFT JOIN sys_user u ON er.upload_user_id = u.id "+
            "WHERE er.id=#{reportId} ")
    Optional<CommonExpReportDTO> findByReportId(@Param("reportId") Long reportId);
    @Select("SELECT " +
            "er.id, " +
            "etc.semester, " +
            "ec.course_name AS courseName, "+
            "ep.id AS projectId, " +
            "ep.project_name AS projectName, " +
            "c.class_name AS className, " +
            "c.id AS classId, "+
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
            "LEFT JOIN sys_user u ON er.upload_user_id = u.id "+
            "WHERE c.id=#{classId} AND ep.id=#{projectId} AND u.user_type=3")
    List<CommonExpReportDTO> findByClassIdProjectId(
            @Param("classId") Long classId,
            @Param("projectId") Long projectId
    );
}
