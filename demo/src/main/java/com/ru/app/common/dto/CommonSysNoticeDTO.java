package com.ru.app.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知数据传输DTO
 * 场景：分页查询返回、批量插入接收
 */
@Data
public class CommonSysNoticeDTO {
    private Long id;              // 主键ID（编辑/删除/返回时用）
    private String noticeTitle;         // 通知标题
    private String noticeContent;       // 通知内容
    private Integer noticeType;   // 通知类型：1=系统 2=学院 3=班级 4=个人
    private Long targetId;        // 目标ID（用户/学院/班级ID）
    private String targetName;   //目标名字
    private Long senderId;        // 发送者ID
    private String senderName; //发送者名字
    private Integer noticeStatus;       // 状态：0=无效 1=有效
    private LocalDateTime createAt; // 创建时间
    private LocalDateTime updateAt; // 更新时间
}