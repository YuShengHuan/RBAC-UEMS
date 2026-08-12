package com.ru.app.common.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ru.app.common.entity.SysPermission;
import com.ru.app.common.entity.SysRole;
import com.ru.app.common.entity.SysUser;
import com.ru.app.common.entity.SysUserRole;
import com.ru.app.module.authority.mapper.AdminSysPermissionMapper;
import com.ru.app.module.authority.mapper.AdminSysRoleMapper;
import com.ru.app.module.authority.mapper.AdminSysUserRoleMapper;
import com.ru.app.module.user.mapper.AdminSysUserMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Service
public class AuthorityService {
    public static final String ROOT="R0000";
    public static final String ADMIN="A1000";
    public static final String TEACHER="T2000";
    public static final String STUDENT="S3000";
    public static final String VISITOR="V4000";

    private final RedisService redisService;
    public final ObjectMapper objectMapper;
    private final AdminSysUserMapper adminSysUserMapper;
    private final AdminSysUserRoleMapper adminSysUserRoleMapper;


    private final AdminSysPermissionMapper adminSysPermissionMapper;
    private final AdminSysRoleMapper adminSysRoleMapper;
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @PostConstruct
    void init(){
          initAllRolePermToRedis();
    }
    public void initAllRolePermToRedis(){
        try {
            setCurrentRolePermission(ROOT);
            setCurrentRolePermission(ADMIN);
            setCurrentRolePermission(TEACHER);
            setCurrentRolePermission(STUDENT);
        }catch (Exception ignored){
            System.out.println("加载异常");
        }
    }
    AuthorityService(
            RedisService redisService,
            ObjectMapper objectMapper,
            AdminSysUserMapper adminSysUserMapper,
            AdminSysUserRoleMapper adminSysUserRoleMapper,
            AdminSysPermissionMapper adminSysPermissionMapper,
            AdminSysRoleMapper adminSysRoleMapper
    ){
        this.redisService=redisService;
        this.objectMapper=objectMapper;
        this.adminSysUserMapper =adminSysUserMapper;
        this.adminSysUserRoleMapper=adminSysUserRoleMapper;
        this.adminSysPermissionMapper=adminSysPermissionMapper;
        this.adminSysRoleMapper=adminSysRoleMapper;
    }
    public void setCurrentRole(String roleCode){
        if(StringUtils.hasText(roleCode)){
            redisService.set("currentRole_"+getUserAccount(),roleCode);
        }
    }
    public void setCurrentRole(String userAccount,String roleCode){
        if(StringUtils.hasText(roleCode)){
            redisService.set("currentRole_"+userAccount,roleCode);
        }
    }
    public void addRolePermInfo(String userAccount,Map<String, Object> userInfo,String roleCode) throws JsonProcessingException {
        this.setCurrentRole(userAccount,roleCode);
        userInfo.put("currentRoleCode",roleCode);
        userInfo.put("currentRoleName",this.getCurrentRoleName(roleCode));
        userInfo.put("selfRolePermissionList",
                this.getCurrentRolePermission(roleCode));
    }
    public String getCurrentRoleFromRedis(){
        return (String) redisService.get("currentRole_"+getUserAccount());
    }
    public boolean hasRole(String roleCode) {
        try {
            return getCurrentRoleFromRedis().equals(roleCode);
        }catch (Exception e){
            return false;
        }
    }
    public List<SysRole> getUserAllRole(Long userId){
        return this.adminSysUserRoleMapper.findAllRoleByUserId(userId);
    }
    public String getCurrentRoleName(String roleCode){
        return this.adminSysRoleMapper.selectRoleNameByRoleCode(roleCode);
    }
    public List<SysPermission> setCurrentRolePermission(String roleCode) throws JsonProcessingException {
        setCurrentRole(roleCode);
        List<SysPermission> sysPermissionList =adminSysPermissionMapper.selectAllPermissionByRoleCode(
                roleCode
        );
        redisService.set("currentRolePermission_"+roleCode,objectMapper.writeValueAsString(sysPermissionList));
        return sysPermissionList;
    }
    public List<SysPermission> getCurrentRolePermission(String roleCode) throws JsonProcessingException {
        String att=(String)redisService.get("currentRolePermission_"+roleCode);
        if(att==null){
            return setCurrentRolePermission(roleCode);
        }
        return objectMapper.readValue(att, new TypeReference<>() {});
    }
    public boolean hasPermByCurrentRoleAndRequestURI(){
        try {
            String requestURI=Objects.requireNonNull(getHttpServletRequest()).getRequestURI();
            List<SysPermission> sysPermissionList =getCurrentRolePermission(getCurrentRoleFromRedis());
            if(sysPermissionList==null||sysPermissionList.isEmpty()){
                return false;
            }
            if(sysPermissionList.stream().filter(item->item.getPermStatus().equals(0)).map(SysPermission::getRequestUri).anyMatch(
                    permUri -> PATH_MATCHER.match(permUri, requestURI)
            )){
                 return true;
            }
            return sysPermissionList.stream()
                    .filter(item->item.getPermStatus().equals(1))
                    .map(SysPermission::getRequestUri) // 提取请求路径
                    .filter(StringUtils::hasText) // 过滤空路径
                    .anyMatch(permUri -> PATH_MATCHER.match(permUri, requestURI)); // 任意匹配即返回true
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    /**
     * 判断当前角色是否存在这个校色
     */
    public SysUserRole selectUserRoleByUserAccountAndRoleCode(String roleCode){
          return adminSysUserRoleMapper.selectUserRoleByUserAccountAndRoleCode(getUserAccount(), roleCode);
    }
    public SysUserRole selectUserRoleByUserAccountAndRoleCode(){
        return adminSysUserRoleMapper.selectUserRoleByUserAccountAndRoleCode(getUserAccount(), getCurrentRoleFromRedis());
    }
    public String getUserAccount() {
        try {
            Object attr = Objects.requireNonNull(getHttpServletRequest()).getAttribute("userAccount");
            if(attr instanceof String){
                return String.valueOf(attr);
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }
    public SysUser getSysUser() {
        return getSysUser(getUserAccount());
    }
    public SysUser getSysUser(String userAccount) {
        try {
            return adminSysUserMapper.selectByUserAccount(userAccount);
        }catch (Exception e){
            return null;
        }
    }
    public HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }
}