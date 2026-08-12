package com.ru.app.common.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知确认实体（对应 sys_notice_confirm 表）
 * 记录用户对通知的确认状态
 */
@Data
@TableName("sys_notice_confirm") // 绑定数据库表名
public class SysNoticeConfirm {

    /** 主键ID（自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 通知ID（关联 sys_notices.id） */
    private Long noticeId;

    /** 用户ID（关联用户表id） */
    private Long userId;

    /** 创建时间（自动填充） */
    private LocalDateTime createAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updateAt;
}