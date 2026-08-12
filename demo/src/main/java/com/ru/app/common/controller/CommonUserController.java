package com.ru.app.common.controller;

import com.ru.app.common.entity.SysRole;
import com.ru.app.common.entity.SysUser;
import com.ru.app.common.dto.ForgotPasswordDTO;
import com.ru.app.common.dto.LoginDTO;
import com.ru.app.common.service.CommonSysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommonUserController {
    private final CommonSysUserService frontSysUserService;

    public CommonUserController(CommonSysUserService frontSysUserService) {
        this.frontSysUserService=frontSysUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        return frontSysUserService.login(loginDTO,response);
    }
    @GetMapping("/email")
    public ResponseEntity<?>  sendEmailCode(@RequestParam String email){
        return frontSysUserService.sendEmailCode(email);
    }
    @PutMapping("/email/update")
    public ResponseEntity<?>  updateEmail(@RequestParam String email,@RequestParam String code){
        return frontSysUserService.updateEmail(email,code);
    }
    @PutMapping("/password/update")
    public ResponseEntity<?>  updatePassword(@RequestParam String password){
        return frontSysUserService.updatePassword(password);
    }
    @PutMapping("/basic-info/update")
    public ResponseEntity<?>  updateBasicInfo(@RequestBody SysUser sysUser){
        return frontSysUserService.updateBasicInfo(sysUser);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?>  forgotPassword(@RequestBody ForgotPasswordDTO dto){
        return frontSysUserService.forgotPasswordReset(dto);
    }
    @PutMapping("/current-role/update")
    public ResponseEntity<?>  updateCurrentRole(@RequestBody SysRole sysRole){
        return frontSysUserService.updateCurrentRole(sysRole);
    }
}
