package com.ru.app.module.authority.controller;

import com.ru.app.module.authority.dto.AdminSysRoleQueryDTO;
import com.ru.app.module.authority.service.AdminSysRoleService;
import com.ru.app.common.entity.SysRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/admin/role") // 接口前缀：系统模块 - 角色
public class AdminSysRoleController {
    private final AdminSysRoleService adminSysRoleService;

    public AdminSysRoleController(AdminSysRoleService adminSysRoleService) {
        this.adminSysRoleService=adminSysRoleService;
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(){
        return adminSysRoleService.selectOption();
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminSysRoleQueryDTO queryDTO){
        return adminSysRoleService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminSysRoleService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SysRole sysRole){
        return adminSysRoleService.create(sysRole);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody  SysRole sysRole){
        return adminSysRoleService.update(sysRole);
    }
    @PostMapping("/batch-insert")
    public ResponseEntity<?> batchInsert(@RequestBody List<SysRole> sysRoleList){
        return adminSysRoleService.batchInsert(sysRoleList);
    }
}