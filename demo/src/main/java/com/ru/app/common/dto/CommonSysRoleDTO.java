package com.ru.app.common.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonSysRoleDTO {
    private Long id;// '角色ID',
    private String roleGroup;//角色分组
    private String roleCode;// '角色编码',
    private String roleName;// '角色名称',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
    private String remark;// '备注'
}
