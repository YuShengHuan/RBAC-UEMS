package com.ru.app.module.user.controller;

import com.ru.app.common.dto.CommonSysUserDTO;
import com.ru.app.module.user.dto.AdminSysUserQueryDTO;
import com.ru.app.module.user.service.AdminSysUserService;
import com.ru.app.common.entity.SysUser;
import com.ru.app.common.utils.EdeUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {
    private final AdminSysUserService adminSysUserService;

    public AdminUserController(AdminSysUserService adminSysUserService) {
        this.adminSysUserService=adminSysUserService;
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(
            @RequestParam(
                    value = "userType",
                    required = false
            )
            Integer userType){
        return adminSysUserService.selectOption(userType);
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminSysUserQueryDTO queryDTO){
        return adminSysUserService.queryPage(queryDTO);
    }
    @PutMapping("/reset-password/{id}")
    public ResponseEntity<?> resetUserPassword(@PathVariable Long id){
        return adminSysUserService.resetPassword(id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminSysUserService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SysUser sysUser){
        sysUser.setUserPassword(EdeUtil.passwordEncrypt(sysUser.getUserPassword()));
        return adminSysUserService.create(sysUser);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody SysUser sysUser){
        if(StringUtils.hasText(sysUser.getUserPassword())){
            sysUser.setUserPassword(EdeUtil.passwordEncrypt(sysUser.getUserPassword()));
        }
        return adminSysUserService.update(sysUser);
    }
    @PostMapping("/batch-insert")
    public ResponseEntity<?> batchInset(@RequestBody List<CommonSysUserDTO> commonSysUserDTOList){
        return adminSysUserService.batchInsert(commonSysUserDTOList);
    }
    @DeleteMapping("/batch-delete")
    public ResponseEntity<?> delete(@RequestBody List<Long> ids){
        return adminSysUserService.batchDelete(ids);
    }

}
