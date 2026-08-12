package com.ru.app.common.service;

import com.ru.app.common.entity.SysPermission;
import com.ru.app.common.entity.SysRole;
import com.ru.app.common.entity.SysUser;
import com.ru.app.common.utils.EdeUtil;
import com.ru.app.common.dto.ForgotPasswordDTO;
import com.ru.app.common.dto.LoginDTO;
import com.ru.app.common.dto.SelfRolePermissionDTO;
import com.ru.app.module.user.mapper.AdminSysUserMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;


@Service
public class CommonSysUserService extends BaseDatabaseService<AdminSysUserMapper,SysUser> {
    private final TokenService tokenService;
    private final EmailService emailService;
    private final AuthorityService authorityService;

    public CommonSysUserService(
            TokenService tokenService,
            EmailService emailService,
            AuthorityService authorityService
    ) {
        this.tokenService = tokenService;
        this.emailService=emailService;
        this.authorityService=authorityService;
    }
    public ResponseEntity<?> login(LoginDTO loginDTO, HttpServletResponse response){
        // 1. 参数非空校验（修复：补充输入合法性校验）
        String userAccount = loginDTO.getUserAccount();
        String userPassword = loginDTO.getUserPassword();
        if (!StringUtils.hasText(userAccount) || !StringUtils.hasText(userPassword)) {
            return ResponseEntity.badRequest().body("用户名或密码不能为空");
        }
        try {
            // 查询用户信息
            SysUser users = authorityService.getSysUser(userAccount);

            // 校验用户是否存在
            if (users == null) {
                return ResponseEntity.badRequest().body("账户不存在");
            }
            // 校验用户状态（修复：新增禁用状态校验）
            Integer userStatus = users.getUserStatus();
            if (Integer.valueOf(0).equals(userStatus)) { // 0:禁用
                return ResponseEntity.badRequest().body("账户已禁用，请联系管理员");
            }
            // 校验密码（修复：避免空指针，明确函数语义）
            if (!EdeUtil.passwordMatch(userPassword, users.getUserPassword())) {
                return ResponseEntity.badRequest().body("密码错误");
            }
            //  根据用户类型查询关联信息（修复：用常量调用equals避免空指针）
            Integer userType = users.getUserType(); // 提取变量，减少重复调用
            // 查询角色和权限（修复：处理rolePermissionVOS为null的情况）
            // 构建用户信息响应体
            Map<String, Object> userInfo = new LinkedHashMap<>();
            userInfo.put("id", users.getId());
            userInfo.put("userAccount", users.getUserAccount());
            userInfo.put("realName", users.getRealName());
            userInfo.put("userType", userType);
            userInfo.put("phone", users.getPhone());
            userInfo.put("email", users.getEmail());
            userInfo.put("gender", users.getGender());
            userInfo.put("userStatus", users.getUserStatus());
            //获取所附加的所有角色
            List<SysRole> selfRoleList=authorityService.getUserAllRole(users.getId());
            userInfo.put("selfRoleList",selfRoleList);
            // 补充管理/教师/学生特有信息
            switch (userType) {
                case 0 -> authorityService.addRolePermInfo(users.getUserAccount(), userInfo, AuthorityService.ROOT);
                case 1 -> authorityService.addRolePermInfo(users.getUserAccount(), userInfo, AuthorityService.ADMIN);
                case 2 -> {
                    userInfo.put("deptId", users.getDeptId());
                    authorityService.addRolePermInfo(users.getUserAccount(), userInfo, AuthorityService.TEACHER);
                }
                case 3 -> {
                    userInfo.put("classId", users.getClassId());
                    authorityService.addRolePermInfo(users.getUserAccount(), userInfo, AuthorityService.STUDENT);
                }
            }
            // 生成并设置token
            tokenService.setRefreshAndAccessToken(response, userAccount);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("登录失败，请重试");
        }
    }
    @Transactional
    public ResponseEntity<?> updateEmail(String email,String emailCode){
        try {
            String userAccount= authorityService.getUserAccount();
            String redisEmailInfo=emailService.getEmailInfoFromRedis(userAccount);
            String redisEmail=redisEmailInfo.split("_")[0];
            String redisEmailCode=redisEmailInfo.split("_")[1];
            if(redisEmail.equals(email)&&redisEmailCode.equals(emailCode)){
                SysUser user=authorityService.getSysUser();
                if(user!=null){
                    user.setEmail(email);
                    emailService.delEmailInfoFromRedis(userAccount);
                    return update(user);
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("验证失败，请尝试");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("验证失败，请尝试");
        }
    }
    public ResponseEntity<?> updateCurrentRole(SysRole sysRole){
        try {
            if(authorityService.selectUserRoleByUserAccountAndRoleCode(sysRole.getRoleCode())!=null){
                List<SysPermission> sysPermissionList =authorityService.setCurrentRolePermission(sysRole.getRoleCode());
                return ResponseEntity.ok(
                        sysPermissionList
                );
            }
            return ResponseEntity.badRequest().body("无效的角色码");
        }catch(Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("验证失败，请尝试");
        }
    }
    @Transactional
    public ResponseEntity<?> updatePassword(String password){
        try {
            SysUser user=authorityService.getSysUser();
            if(user!=null){
                user.setUserPassword(EdeUtil.passwordEncrypt(password));
                return update(user);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("验证失败，请尝试");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("验证失败，请尝试");
        }
    }
    @Transactional
    public ResponseEntity<?> updateBasicInfo(SysUser sysUser){
        try {
            SysUser user=authorityService.getSysUser();
            if(user!=null){
                if(StringUtils.hasText(sysUser.getRealName())){
                     user.setRealName(sysUser.getRealName());
                }
                if(sysUser.getGender()!=null){
                    user.setGender(sysUser.getGender());
                }
                if(StringUtils.hasText(sysUser.getPhone())){
                    user.setPhone(sysUser.getPhone());
                }
                if(sysUser.getClassId()!=null){
                    user.setClassId(sysUser.getClassId());
                    return update(user);
                }
                if(sysUser.getDeptId()!=null){
                    user.setDeptId(sysUser.getDeptId());
                    return update(user);
                }
                return update(user);
            }
            return ResponseEntity.badRequest().body("验证失败，请尝试");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("验证失败，请尝试");
        }
    }
    public ResponseEntity<?> sendEmailCode(String email){
        try {
            emailService.send(email);
            return ResponseEntity.ok().body("发送成功");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("发送异常失败，请尝试");
        }
    }
    public ResponseEntity<?> forgotPasswordReset(ForgotPasswordDTO dto){
        try {
            SysUser sysUser=authorityService.getSysUser(dto.getUserAccount());
            if(sysUser!=null&&dto.getStep()!=null){
                if(dto.getStep().equals(0)){
                    String email=emailService.desensitizeEmail(sysUser.getEmail());
                    if(email!=null){
                        return ResponseEntity.ok(Collections.singletonMap("email",email));
                    }
                    return ResponseEntity.badRequest().body("邮箱异常");
                }
                else{
                    String redisEmailInfo=emailService.getEmailInfoFromRedis(dto.getUserAccount());
                    if (dto.getStep().equals(1)){
                        if(redisEmailInfo==null){
                            if(dto.getEmail().equals(sysUser.getEmail())){
                                emailService.send(dto.getUserAccount(),dto.getEmail(),false);
                                return ResponseEntity.ok("邮箱发送成功，请查收");
                            }
                            return ResponseEntity.badRequest().body("邮箱异常");
                        }else{
                            String redisEmail=redisEmailInfo.split("_")[0];
                            String redisEmailCode=redisEmailInfo.split("_")[1];
                            String redisIsSuccessSure=redisEmailInfo.split("_")[2];
                            if(redisEmail.equals(dto.getEmail())&&redisEmailCode.equals(dto.getCode())&&redisIsSuccessSure.equals("false")){
                                emailService.setEmailInfoToRedis(dto.getUserAccount(),dto.getEmail(),dto.getCode(),true);
                                return ResponseEntity.ok("邮箱验证成功");
                            }
                            return ResponseEntity.badRequest().body("邮箱验证失败");
                        }
                    }
                    else if (dto.getStep().equals(2)){
                        String redisIsSuccessSure=redisEmailInfo.split("_")[2];
                        System.out.println(redisIsSuccessSure);
                        if(redisIsSuccessSure!=null&&redisIsSuccessSure.equals("true")){
                            emailService.delEmailInfoFromRedis(dto.getUserAccount());
                            sysUser.setUserPassword(EdeUtil.passwordEncrypt(dto.getPassword()));
                            return update(sysUser);
                        }
                    }
                }
            }else{
                return ResponseEntity.badRequest().body("用户不存在");
            }
            return ResponseEntity.badRequest().body("找回失败，请尝试");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("找回异常，请尝试");
        }
    }
}
