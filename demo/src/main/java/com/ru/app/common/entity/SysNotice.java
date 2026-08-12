package com.ru.app.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统通知实体（对应 sys_notice 表）
 * 支持：系统消息、个人消息、学院消息、班级消息
 */
@Data  // Lombok 自动生成 getter/setter/toString 等
@TableName("sys_notice")  // 绑定数据库表名（与之前设计的表一致）
public class SysNotice {

    /** 主键ID（自增） */
    @TableId(type = IdType.AUTO)  // 自增主键策略（匹配表结构 BIGINT UNSIGNED AUTO_INCREMENT）
    private Long id;

    /** 通知标题（非空） */
    private String noticeTitle;

    /** 通知内容（非空） */
    private String noticeContent;

    /** 通知类型（1=系统消息，2=个人消息，3=学院消息，4=班级消息） */
    private Integer noticeType;

    /** 目标ID：用户ID（个人消息）/学院ID（学院消息）/班级ID（班级消息），系统消息为NULL */
    private Long targetId;

    /** 发送者ID：系统消息为NULL，其他消息为发送者用户ID */
    private Long senderId;

    /** 状态（0=无效，1=有效），默认1 */
    private Integer noticeStatus = 1;

    /** 创建时间（自动填充） */
    private LocalDateTime createAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updateAt;
}