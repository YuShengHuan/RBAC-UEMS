package com.ru.app.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统字典实体（对应 sys_dict 表）
 */
@Data  // Lombok 自动生成 getter/setter/toString 等
@TableName("sys_dict")  // 绑定数据库表名
public class SysDict {

    /** 主键ID（自增） */
    @TableId(type = IdType.AUTO)  // 自增主键策略
    private Long id;

    /** 字典类型（业务标识，如：weather=天气类型、user_status=用户状态） */
    private String dictGroup;

    /** 字典键（分组内唯一，如：sun=晴天、normal=正常用户） */
    private String dictKey;

    /** 字典值（展示用，如：“晴天”“正常用户”） */
    private String dictValue;

    /** 字典描述（便于维护） */
    private String remark;

    /** 创建时间（自动填充） */
    private LocalDateTime createdAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updatedAt;
}