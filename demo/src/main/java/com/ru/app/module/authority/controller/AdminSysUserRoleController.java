package com.ru.app.module.authority.controller;
import com.ru.app.module.authority.dto.AdminSysUserRoleQueryDTO;
import com.ru.app.module.authority.service.AdminSysUserRoleService;
import com.ru.app.common.entity.SysUserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/user-role")
public class AdminSysUserRoleController {
    private final AdminSysUserRoleService adminSysUserRoleService;

    public AdminSysUserRoleController(AdminSysUserRoleService adminSysUserRoleService) {
        this.adminSysUserRoleService=adminSysUserRoleService;
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminSysUserRoleQueryDTO queryDTO){
        return adminSysUserRoleService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminSysUserRoleService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SysUserRole sysUserRole){
        return adminSysUserRoleService.create(sysUserRole);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody  SysUserRole sysUserRole){
        return adminSysUserRoleService.update(sysUserRole);
    }
}