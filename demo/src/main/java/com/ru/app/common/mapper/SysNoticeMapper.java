package com.ru.app.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysNoticeDTO;
import com.ru.app.common.entity.SysNotice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface SysNoticeMapper extends BaseMapper<SysNotice> {
    /**
     * 通知分页查询（返回DTO格式，仅包含前端所需字段）
     * @param page 分页参数（页码、每页条数）
     * @param wrapper 动态查询条件（来自Service构建）
     * @return 分页结果（IPage<SysNoticeDTO>）
     */
    @Select("""
        SELECT 
            sn.id,
            sn.notice_title AS noticeTitle,
            sn.notice_content AS noticeContent,
            sn.notice_type AS noticeType,
            sn.target_id AS targetId,
            CASE
                WHEN sn.notice_type = 2 THEN d.dept_name  -- 学院消息：取学院名
                WHEN sn.notice_type = 3 THEN c.class_name       -- 班级消息：取班级名
                WHEN sn.notice_type = 4 THEN u1.real_name       -- 个人消息：取用户名
                ELSE ''  -- 系统消息（notice_type=1）：targetName为空
            END AS targetName,
            sn.sender_id AS senderId,
            -- senderId 关联用户表（系统消息 sender_id 为null，显示“系统”）
            COALESCE(u2.real_name, '系统') AS senderName,
            sn.notice_status AS noticeStatus,
            sn.create_at AS createAt,
            sn.update_at AS updateAt
        FROM sys_notice sn
        LEFT JOIN base_dept d ON sn.notice_type =2 AND sn.target_id = d.id
        LEFT JOIN base_class c ON sn.notice_type = 3 AND sn.target_id = c.id
        LEFT JOIN sys_user u1 ON sn.notice_type = 4 AND sn.target_id = u1.id
        LEFT JOIN sys_user u2 ON sn.sender_id = u2.id
        ${ew.customSqlSegment}
        ORDER BY sn.update_at DESC, sn.create_at DESC
    """)
    IPage<CommonSysNoticeDTO> queryPage(
            Page<CommonSysNoticeDTO> page,
            @Param(Constants.WRAPPER) Wrapper<SysNotice> wrapper
    );

    @Select("""
        SELECT 
            sn.id,
            sn.notice_title AS noticeTitle,
            sn.notice_content AS noticeContent,
            sn.notice_type AS noticeType,
            sn.target_id AS targetId,
            CASE
                WHEN sn.notice_type = 2 THEN d.dept_name  -- 学院消息：取学院名
                WHEN sn.notice_type = 3 THEN c.class_name       -- 班级消息：取班级名
                WHEN sn.notice_type = 4 THEN u1.real_name       -- 个人消息：取用户名
                ELSE ''  -- 系统消息（notice_type=1）：targetName为空
            END AS targetName,
            sn.sender_id AS senderId,
            -- senderId 关联用户表（系统消息 sender_id 为null，显示“系统”）
            COALESCE(u2.real_name, '系统') AS senderName,
            sn.notice_status AS noticeStatus,
            sn.create_at AS createAt,
            sn.update_at AS updateAt
        FROM sys_notice sn
        LEFT JOIN base_dept d ON sn.notice_type =2 AND sn.target_id = d.id
        LEFT JOIN base_class c ON sn.notice_type = 3 AND sn.target_id = c.id
        LEFT JOIN sys_user u1 ON sn.notice_type = 4 AND sn.target_id = u1.id
        LEFT JOIN sys_user u2 ON sn.sender_id = u2.id
        ${ew.customSqlSegment}
        ORDER BY sn.update_at DESC, sn.create_at DESC
    """)
    List<CommonSysNoticeDTO> selectOption(
            @Param(Constants.WRAPPER) Wrapper<SysNotice> wrapper
    );
}