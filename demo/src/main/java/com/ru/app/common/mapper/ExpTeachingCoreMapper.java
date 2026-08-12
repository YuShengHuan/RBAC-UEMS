package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonExpTeachingCoreDTO;
import com.ru.app.common.entity.ExpTeachingCore;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ExpTeachingCoreMapper extends BaseMapper<ExpTeachingCore> {
    @Select("SELECT " +
            "etc.id, " +
            "etc.semester, " +
            "u.user_account AS userAccount, " +    // 关联教师工号
            "u.real_name AS realName, " +
            "u.id AS userId, " +
            "ec.course_name AS courseName, " +
            "ec.id AS courseId, " +
            "c.class_name AS className, " +
            "c.id AS classId, " +
            "etc.create_at AS createAt, " +
            "etc.update_at AS updateAt " +
            "FROM exp_teaching_core etc " +
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id " +
            "LEFT JOIN base_class c ON etc.class_id = c.id " +
            "LEFT JOIN sys_user u ON etc.user_id = u.id " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY etc.update_at DESC, etc.create_at DESC" +
            "")
        // 注入动态条件（来自Wrapper）
    IPage<CommonExpTeachingCoreDTO> queryPage(
            Page<CommonExpTeachingCoreDTO> page,
            @Param(Constants.WRAPPER) Wrapper<ExpTeachingCore> wrapper
    );

    @Select("SELECT " +
            "etc.id, " +
            "etc.semester, " +
            "u.user_account AS userAccount, " +    // 关联教师工号
            "u.real_name AS realName, " +
            "u.id AS userId, " +
            "ec.course_name AS courseName, " +
            "ec.id AS courseId, " +
            "c.class_name AS className, " +
            "c.id AS classId, " +
            "etc.create_at AS createAt, " +
            "etc.update_at AS updateAt " +
            "FROM exp_teaching_core etc " +
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id " +
            "LEFT JOIN base_class c ON etc.class_id = c.id " +
            "LEFT JOIN sys_user u ON etc.user_id = u.id " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY etc.update_at DESC, etc.create_at DESC" +
            "")
        // 注入动态条件（来自Wrapper）
    List<CommonExpTeachingCoreDTO> queryAll(@Param(Constants.WRAPPER) QueryWrapper<ExpTeachingCore> wrapper);
    @Select("SELECT " +
            "etc.id, " +
            "etc.semester, " +
            "u.user_account AS userAccount, " +    // 关联教师工号
            "u.real_name AS realName, " +
            "u.id AS userId, " +
            "ec.course_name AS courseName, " +
            "ec.id AS courseId, " +
            "c.class_name AS className, " +
            "c.id AS classId, " +
            "etc.create_at AS createAt, " +
            "etc.update_at AS updateAt " +
            "FROM exp_teaching_core etc " +
            "LEFT JOIN exp_course ec ON etc.course_id = ec.id " +
            "LEFT JOIN base_class c ON etc.class_id = c.id " +
            "LEFT JOIN sys_user u ON etc.user_id = u.id " +
            "${ew.customSqlSegment}"+
            // 新增：按更新时间降序，更新时间为空则按创建时间降序（最新在前）
            "ORDER BY etc.update_at DESC, etc.create_at DESC" +
            "")
        // 注入动态条件（来自Wrapper）
    CommonExpTeachingCoreDTO queryDetail(@Param(Constants.WRAPPER) QueryWrapper<ExpTeachingCore> wrapper);
}
