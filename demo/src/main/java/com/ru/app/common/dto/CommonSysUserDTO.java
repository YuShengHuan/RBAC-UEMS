package com.ru.app.common.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonSysUserDTO {
    private Long id;  //'用户ID',
    private String userAccount;  //'账号（工号/学号）',
    private String userPassword;  //'加密密码',
    private String realName;  //'真实姓名',
    private Integer gender;  //'性别(男,女)',
    private String phone;  //'联系电话',
    private String email;  //'电子邮箱',
    private Integer userType;  //'用户类型（2-教师，3-学生）',
    private Long deptId; //所在学院
    private String deptName;//所在学院name
    private Long classId; //所在班级
    private String className;//所在班级name
    private Integer userStatus;  //'状态(1:启用,0:禁用)',
    private String remark;  //'备注'
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
