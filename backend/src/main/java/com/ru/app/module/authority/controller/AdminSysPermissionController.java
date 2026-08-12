package com.ru.app.module.authority.controller;

import com.ru.app.module.authority.dto.AdminSysPermissionQueryDTO;
import com.ru.app.module.authority.service.AdminSysPermissionService;
import com.ru.app.common.entity.SysPermission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/admin/perm") // 接口前缀：系统模块 - 权限
public class AdminSysPermissionController {
    private final AdminSysPermissionService adminSysPermissionService;

    public AdminSysPermissionController(AdminSysPermissionService adminSysPermissionService) {
        this.adminSysPermissionService=adminSysPermissionService;
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(
            @RequestParam(
                    value = "isTop",
                    required = false
            ) Integer isTop
    ){
        return adminSysPermissionService.selectOption(isTop);
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminSysPermissionQueryDTO queryDTO){
        return adminSysPermissionService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminSysPermissionService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SysPermission sysPermission){
        return adminSysPermissionService.create(sysPermission);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody SysPermission sysPermission){
        return adminSysPermissionService.update(sysPermission);
    }
    @PostMapping("/batch-insert")
    public ResponseEntity<?> batchInset(@RequestBody List<SysPermission> sysPermissionList){
        return adminSysPermissionService.batchInsert(sysPermissionList);
    }
    @DeleteMapping("/batch-delete")
    public ResponseEntity<?> delete(@RequestBody List<Long> ids){
        return adminSysPermissionService.batchDelete(ids);
    }
    @GetMapping("/tree/{roleId}")
    public ResponseEntity<?> permTree(
            @PathVariable Long roleId
    ){
         return ResponseEntity.ok(
                 adminSysPermissionService.buildPermissionTreeWithOwnFlag(
                         roleId
                 )
         );
    }
}
