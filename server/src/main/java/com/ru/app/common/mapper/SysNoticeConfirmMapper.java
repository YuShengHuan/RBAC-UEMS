package com.ru.app.common.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysNoticeConfirmDTO;
import com.ru.app.common.entity.SysNoticeConfirm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface SysNoticeConfirmMapper extends BaseMapper<SysNoticeConfirm> {
    /**
     * 分页查询通知确认记录（关联通知表+用户表，返回前端所需字段）
     */
    @Select("""
        SELECT 
            sc.id,
            sc.notice_id AS noticeId,
            sc.user_id AS userId,
            sn.notice_title AS noticeTitle,  -- 关联通知表：通知标题
            sn.notice_content AS noticeContent,  -- 关联通知表：通知内容
            sn.notice_type AS noticeType,
            u.user_account AS userAccount,   -- 关联用户表：账户
            u.real_name AS realName,   -- 关联用户表：用户真实名
            sc.create_at AS createAt,
            sc.update_at AS updateAt
        FROM sys_notice_confirm sc
        -- 关联通知表（必关联，确认记录依赖通知）
        INNER JOIN sys_notice sn ON sc.notice_id = sn.id
        -- 关联用户表（左关联，兼容用户表数据缺失场景）
        LEFT JOIN sys_user u ON sc.user_id = u.id
        -- 动态筛选条件（来自QueryWrapper）
        ${ew.customSqlSegment}
        -- 排序：最新确认优先
        ORDER BY sc.create_at DESC
    """)
    IPage<CommonSysNoticeConfirmDTO> queryPage(
            Page<CommonSysNoticeConfirmDTO> page,
            @Param(Constants.WRAPPER) Wrapper<SysNoticeConfirm> wrapper
    );
}