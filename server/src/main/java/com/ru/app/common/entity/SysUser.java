package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ru.app.common.utils.EdeUtil;
import jakarta.annotation.PostConstruct;
import lombok.Data;

import java.time.LocalDateTime;

@Data  // Lombok 注解，自动生成 getter/setter
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long id;  //'用户ID',
    private String userAccount;  //'账号（工号/学号）',
    private String userPassword;  //'加密密码',
    private String realName;  //'真实姓名',
    private Integer gender;  //'性别(男,女)',
    private String phone;  //'联系电话',
    private String email;  //'电子邮箱',
    private Integer userType;  //'用户类型（1-管理员，2-教师，3-学生）',
    private Long deptId; //所在学院
    private Long classId; //所在班级
    private Integer userStatus = 1;  //'状态(1:启用,0:禁用)',
    private String remark;  //'备注'
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
