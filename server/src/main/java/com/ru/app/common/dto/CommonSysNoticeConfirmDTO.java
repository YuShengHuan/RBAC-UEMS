package com.ru.app.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知确认记录返回DTO
 * 包含前端展示所需的核心字段（关联通知标题，避免前端二次查询）
 */
@Data
public class CommonSysNoticeConfirmDTO {
    private Long id;
    private Long noticeId;
    private String noticeTitle;
    private Integer noticeType;   // 通知类型：1=系统 2=学院 3=班级 4=个人
    private String noticeContent;       // 通知内容
    private Long userId;
    private String userAccount;
    private String realName;
    private LocalDateTime createAt; // 创建时间
    private LocalDateTime updateAt; // 更新时间
}