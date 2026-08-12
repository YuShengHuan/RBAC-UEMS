package com.ru.app.module.authority.controller;

import com.ru.app.common.entity.SysRole;
import com.ru.app.module.authority.dto.AdminSysRolePermissionQueryDTO;
import com.ru.app.module.authority.service.AdminSysRolePermissionService;
import com.ru.app.common.entity.SysRolePermission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/role-perm")
public class AdminSysRolePermissionController {
    private final AdminSysRolePermissionService adminSysRolePermissionService;

    public AdminSysRolePermissionController(AdminSysRolePermissionService adminSysRolePermissionService) {
        this.adminSysRolePermissionService=adminSysRolePermissionService;
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminSysRolePermissionQueryDTO queryDTO){
        return adminSysRolePermissionService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminSysRolePermissionService.delete(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SysRolePermission sysRolePermission){
        return adminSysRolePermissionService.create(sysRolePermission);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody  SysRolePermission sysRolePermission){
        return adminSysRolePermissionService.update(sysRolePermission);
    }
    @PostMapping("/{roleId}/batch-insert")
    public ResponseEntity<?> batchInsertByRoleId(
            @PathVariable Long roleId,
            @RequestBody List<SysRolePermission> sysRolePermissionList){
        return adminSysRolePermissionService.batchInsert(
                roleId,
                sysRolePermissionList
        );
    }
}