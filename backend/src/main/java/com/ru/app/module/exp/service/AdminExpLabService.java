package com.ru.app.module.exp.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonExpLabDTO;
import com.ru.app.common.dto.CommonSysPermissionDTO;
import com.ru.app.common.entity.SysPermission;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.exp.dto.AdminExpLabQueryDTO;
import com.ru.app.module.exp.mapper.AdminExpLabMapper;
import com.ru.app.common.dto.SelectOptionDTO;
import com.ru.app.common.entity.ExpLab;
import com.ru.app.common.service.BaseDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminExpLabService extends BaseDatabaseService<AdminExpLabMapper, ExpLab> {
    private final AuthorityService authorityService;
    AdminExpLabService(
            AuthorityService authorityService
    ){
        this.authorityService=authorityService;
    }
    public ResponseEntity<?> selectOption(){
        try {
            QueryWrapper<ExpLab> wrapper = new QueryWrapper<>();
            applyRoleFilterForOption(wrapper);
            List<CommonExpLabDTO> list=baseMapper.queryAll(wrapper);
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();
            for(CommonExpLabDTO adminExpLabDTO:list){
                SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                selectOptionDTO.setLabel(
                        adminExpLabDTO.getLabLocation()+
                                "["+
                                adminExpLabDTO.getLabCode()+","+
                                adminExpLabDTO.getLabName()+","+
                                adminExpLabDTO.getDeptName()+
                                "]"
                );
                selectOptionDTO.setValue(
                        adminExpLabDTO.getId().toString()
                );
                selectOptionDTOS.add(selectOptionDTO);
            }
            return ResponseEntity.ok(
                    selectOptionDTOS
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> queryPage(AdminExpLabQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonExpLabDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<ExpLab> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getLabCode())){
                wrapper.like("b.lab_code",queryDTO.getLabCode());
            }
            if(StringUtils.hasText(queryDTO.getLabName())){
                wrapper.like("b.lab_name",queryDTO.getLabName());
            }
            if(StringUtils.hasText(queryDTO.getLabLocation())){
                wrapper.like("b.lab_location",queryDTO.getLabLocation());
            }
            applyRoleFilterForQuery(wrapper);
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> batchInsert(List<ExpLab> expLabList) {
        try {
            if(authorityService.hasRole(AuthorityService.ADMIN)){
                Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
                expLabList=expLabList.stream().peek(
                        item-> item.setDeptId(deptId)
                ).toList();
                //先获取所有实验室，筛选出自己学院的实验室
                QueryWrapper<ExpLab> wrapper=new QueryWrapper<>();
                wrapper.eq("b.dept_id",deptId);
                List<CommonExpLabDTO> commonExpLabDTOList=baseMapper.queryAll(wrapper);
                //以permCode为主键，先排查存在的，不存在的放一边
                List<ExpLab> noExitInDb=expLabList.stream().filter(
                        item-> commonExpLabDTOList.stream().noneMatch(
                                w-> w.getLabName().equals(item.getLabName())&&
                                    w.getLabLocation().equals(item.getLabLocation())
                        )
                ).toList();
                List<ExpLab> exitInDb=expLabList.stream().filter(
                        item-> commonExpLabDTOList.stream().anyMatch(
                                w-> w.getLabName().equals(item.getLabName())&&
                                    w.getLabLocation().equals(item.getLabLocation())
                        )
                ).toList();
                System.out.println("不存在的数量："+noExitInDb.size());
                System.out.println("存在的数量："+exitInDb.size());
                boolean status= saveBatch(noExitInDb);
                if(status){
                    return ResponseEntity.ok().body("插入成功");
                }
                return ResponseEntity.badRequest().body("插入失败");
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("没得权限干这事");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    private void applyRoleFilterForQuery(QueryWrapper<ExpLab> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.eq("d.id",deptId);
        }
        else{
            throw new Exception("权限不足");
        }
    }
    public void applyRoleFilterForOption(QueryWrapper<ExpLab> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.eq("d.id",deptId);
        }else if (authorityService.hasRole(AuthorityService.TEACHER)){
            Long deptId=authorityService.getSysUser().getDeptId();
            wrapper.eq("d.id",deptId);
        }
        else{
            throw new Exception("权限不足");
        }
    }
}
