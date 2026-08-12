package com.ru.app.module.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonBaseClassDTO;
import com.ru.app.common.dto.CommonBaseDeptDTO;
import com.ru.app.common.dto.CommonSysUserDTO;
import com.ru.app.common.entity.BaseClass;
import com.ru.app.common.entity.BaseDept;
import com.ru.app.common.entity.ExpCourse;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.base.mapper.AdminBaseClassMapper;
import com.ru.app.module.base.mapper.AdminBaseDeptMapper;
import com.ru.app.module.user.dto.AdminSysUserQueryDTO;
import com.ru.app.module.user.mapper.AdminSysUserMapper;
import com.ru.app.common.dto.SelectOptionDTO;
import com.ru.app.common.entity.SysUser;
import com.ru.app.common.service.BaseDatabaseService;
import com.ru.app.common.utils.EdeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminSysUserService extends BaseDatabaseService<AdminSysUserMapper, SysUser> {
    private final AuthorityService authorityService;
    private final AdminBaseClassMapper adminBaseClassMapper;
    private final AdminBaseDeptMapper adminBaseDeptMapper;
    AdminSysUserService(
            AuthorityService authorityService,
            AdminBaseClassMapper adminBaseClassMapper,
            AdminBaseDeptMapper adminBaseDeptMapper
    ){
        this.authorityService=authorityService;
        this.adminBaseClassMapper=adminBaseClassMapper;
        this.adminBaseDeptMapper=adminBaseDeptMapper;
    }
    public ResponseEntity<?> selectOption(Integer userType){
        try {
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            if(userType!=null){
                wrapper.eq("u.user_type",userType);
            }
            applyRoleFilterForOption(wrapper);
            List<CommonSysUserDTO> list=baseMapper.queryAll(wrapper);
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();
            for(CommonSysUserDTO adminSysUserDTO:list){
                SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                selectOptionDTO.setLabel(
                        adminSysUserDTO.getRealName()+
                                "["+
                                adminSysUserDTO.getUserAccount()+
                                "]"
                );
                selectOptionDTO.setValue(
                        adminSysUserDTO.getId().toString()
                );
                selectOptionDTOS.add(selectOptionDTO);
            }
            return ResponseEntity.ok(
                    selectOptionDTOS
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    public ResponseEntity<?> queryPage(AdminSysUserQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonSysUserDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getUserAccount())){
                wrapper.like("u.user_account",queryDTO.getUserAccount());
            }
            if(StringUtils.hasText(queryDTO.getRealName())){
                wrapper.like("u.real_name",queryDTO.getRealName());
            }
            if(queryDTO.getUserType()!=null){
                wrapper.eq("u.user_type",queryDTO.getUserType());
            }
            if(StringUtils.hasText(queryDTO.getDeptName())){
                wrapper.apply("d2.dept_name LIKE {0}",queryDTO.getDeptName());
            }
            if(StringUtils.hasText(queryDTO.getDeptName())){
                wrapper.apply("c.class_name LIKE {0}",queryDTO.getClassName());
            }
            applyRoleFilterForQuery(wrapper);
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    public ResponseEntity<?> resetPassword(Long id){
        try {
            if(id==null){
                return ResponseEntity.badRequest().body("id不能为null");
            }
            SysUser sysUser=baseMapper.selectById(id);
            if(sysUser==null){
                return ResponseEntity.badRequest().body("不存在数据");
            }
            String reset="klxy@"+sysUser.getUserAccount();
            sysUser.setUserPassword(
                    EdeUtil.passwordEncrypt(reset)
            );
            boolean status=updateById(sysUser);
            if(status){
                return ResponseEntity.ok().body("重置成功："+reset);
            }else{
                return ResponseEntity.badRequest().body("重置失败");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    public ResponseEntity<?> batchInsert(List<CommonSysUserDTO> commonSysUserDTOList) {
        try {
            if(authorityService.hasRole(AuthorityService.ADMIN)) {
                Long deptId = authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
                //获取自己学院的班级
                QueryWrapper<BaseClass> baseClassQueryWrapper=new QueryWrapper<>();
                baseClassQueryWrapper.eq("d.id",deptId);
                List<CommonBaseClassDTO> commonBaseClassDTOList=adminBaseClassMapper.queryAll(baseClassQueryWrapper);
                //获取自己的学院
                BaseDept baseDept=adminBaseDeptMapper.selectById(deptId);
                //获取当前已经存在的用户
                QueryWrapper<SysUser> sysUserQueryWrapper=new QueryWrapper<>();
                sysUserQueryWrapper.and(w ->w.eq("d2.id",deptId).or().eq("d1.id",deptId));
                List<CommonSysUserDTO> dbDTOList=baseMapper.queryAll(sysUserQueryWrapper);


                //以userAccount为主键，先排查存在的，不存在的放一边
                List<CommonSysUserDTO> noExitInDb=commonSysUserDTOList.stream().filter(
                        item-> dbDTOList.stream().noneMatch(
                                w-> w.getUserAccount().equals(item.getUserAccount())
                        )
                ).toList();
                List<CommonSysUserDTO> exitInDb=commonSysUserDTOList.stream().filter(
                        item-> dbDTOList.stream().anyMatch(
                                w-> w.getUserAccount().equals(item.getUserAccount())
                        )
                ).toList();
                List<SysUser> saveSysUserList=noExitInDb.stream().map(
                        item->{
                              if(StringUtils.hasText(item.getDeptName())&&item.getUserType().equals(2)){
                                    if(item.getDeptName().equals(baseDept.getDeptName())){
                                         item.setDeptId(baseDept.getId());
                                    }
                              }
                              if(StringUtils.hasText(item.getClassName())&&item.getUserType().equals(3)){
                                   Optional<CommonBaseClassDTO> baseClassF= commonBaseClassDTOList.stream().filter(cdb->
                                           cdb.getClassName().equals(item.getClassName())).findFirst();
                                  baseClassF.ifPresent(commonBaseClassDTO -> item.setClassId(commonBaseClassDTO.getId()));
                              }
                              item.setUserPassword(
                                      EdeUtil.passwordEncrypt(item.getUserPassword())
                              );
                              SysUser sysUser=new SysUser();
                              BeanUtils.copyProperties(item,sysUser);
                              return sysUser;
                        }
                ).toList();
                //检查看看存不存在重复的主键
                int size=saveSysUserList.stream().map(SysUser::getUserAccount).collect(Collectors.toSet()).size();
                if(size!=saveSysUserList.size()){
                    return ResponseEntity.badRequest().body("存在重复的用户名");
                }
                System.out.println("不存在的数量："+noExitInDb.size());
                System.out.println("存在的数量："+exitInDb.size());

                boolean status= saveBatch(saveSysUserList);
                if(status){
                    return ResponseEntity.ok().body("插入成功");
                }
                return ResponseEntity.badRequest().body("插入失败");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("没得权限干这事");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }

    private void applyRoleFilterForQuery(QueryWrapper<SysUser> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.and(w ->w.eq("d2.id",deptId).or().eq("d1.id",deptId));
        }else if(authorityService.hasRole(AuthorityService.TEACHER)){
            Long userId=authorityService.getSysUser().getId();
            wrapper.eq("u.id",userId);
        }
        else if(authorityService.hasRole(AuthorityService.STUDENT)){
            Long userId=authorityService.getSysUser().getId();
            wrapper.eq("u.id",userId);
        }
        else{
            throw new Exception("权限不足");
        }
    }
    public void applyRoleFilterForOption(QueryWrapper<SysUser> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.and(w ->w.eq("d2.id",deptId).or().eq("d1.id",deptId));
        }else if(authorityService.hasRole(AuthorityService.TEACHER)){
            Long userId=authorityService.getSysUser().getId();
            wrapper.eq("u.id",userId);
        }
        else if(authorityService.hasRole(AuthorityService.STUDENT)){
            Long userId=authorityService.getSysUser().getId();
            wrapper.eq("u.id",userId);
        }
        else{
            throw new Exception("权限不足");
        }
    }
}
