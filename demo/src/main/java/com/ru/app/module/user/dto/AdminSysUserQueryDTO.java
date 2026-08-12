package com.ru.app.module.user.dto;
import com.ru.app.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class AdminSysUserQueryDTO extends PageQueryDTO {
    private String userAccount;  //'账号（工号/学号）',
    private String realName;  //'真实姓名',
    private String deptName;//所在学院name
    private String className;//所在班级name
    private Integer userType;//用户类型
}
