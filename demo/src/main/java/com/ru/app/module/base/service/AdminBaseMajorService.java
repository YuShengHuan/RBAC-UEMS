package com.ru.app.module.base.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonBaseMajorDTO;
import com.ru.app.common.dto.CommonSysUserDTO;
import com.ru.app.common.entity.BaseDept;
import com.ru.app.common.entity.SysUser;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.base.dto.AdminBaseMajorQueryDTO;
import com.ru.app.module.base.mapper.AdminBaseMajorMapper;
import com.ru.app.common.dto.SelectOptionDTO;
import com.ru.app.common.entity.BaseMajor;
import com.ru.app.common.service.BaseDatabaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminBaseMajorService extends BaseDatabaseService<AdminBaseMajorMapper, BaseMajor> {
    private final AuthorityService authorityService;
    AdminBaseMajorService(
            AuthorityService authorityService
    ){
        this.authorityService=authorityService;
    }
    public ResponseEntity<?> selectOption(){
        try {
            QueryWrapper<BaseMajor> wrapper = new QueryWrapper<>();

            applyRoleFilterForOption(wrapper);

            List<CommonBaseMajorDTO> list=baseMapper.queryAll(wrapper);
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();
            for(CommonBaseMajorDTO adminBaseMajorDTO:list){
                SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                selectOptionDTO.setLabel(
                        adminBaseMajorDTO.getMajorName()+
                                "["+
                                adminBaseMajorDTO.getMajorCode()+","+
                                adminBaseMajorDTO.getDeptName()+
                                "]"
                );
                selectOptionDTO.setValue(
                        adminBaseMajorDTO.getId().toString()
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
    public ResponseEntity<?> queryPage(AdminBaseMajorQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonBaseMajorDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<BaseMajor> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getMajorCode())){
                wrapper.like("m.major_code",queryDTO.getMajorCode());
            }
            if(StringUtils.hasText(queryDTO.getMajorName())){
                wrapper.like("m.major_name",queryDTO.getMajorName());
            }
            applyRoleFilterForQuery(wrapper);
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> batchInsert(List<CommonBaseMajorDTO> commonBaseMajorDTOList) {
        try {
            if(authorityService.hasRole(AuthorityService.ADMIN)) {
                Long deptId = authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();


                QueryWrapper<BaseMajor> baseMajorQueryWrapper=new QueryWrapper<>();
                baseMajorQueryWrapper.eq("m.dept_id",deptId);
                List<CommonBaseMajorDTO> dbDTOList=baseMapper.queryAll(baseMajorQueryWrapper);


                //以majorName为主键，先排查存在的，不存在的放一边
                List<CommonBaseMajorDTO> noExitInDb=commonBaseMajorDTOList.stream().filter(
                        item-> dbDTOList.stream().noneMatch(
                                w-> w.getMajorName().equals(item.getMajorName())
                        )
                ).toList();
                List<CommonBaseMajorDTO> exitInDb=commonBaseMajorDTOList.stream().filter(
                        item-> dbDTOList.stream().anyMatch(
                                w-> w.getMajorName().equals(item.getMajorName())
                        )
                ).toList();

                List<BaseMajor> saveBaseMajorList = noExitInDb.stream().map(
                        item -> {
                            item.setDeptId(deptId);
                            BaseMajor baseMajor = new BaseMajor();
                            BeanUtils.copyProperties(item, baseMajor);
                            return baseMajor;
                        }
                ).toList();

                boolean status = saveBatch(saveBaseMajorList);
                if (status) {
                    return ResponseEntity.ok().body("插入成功");
                } else {
                    return ResponseEntity.badRequest().body("插入失败");
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("没得权限干这事");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    private void applyRoleFilterForQuery(QueryWrapper<BaseMajor> wrapper) throws Exception {
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
    private void applyRoleFilterForOption(QueryWrapper<BaseMajor> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.eq("d.id",deptId);
        }else if (authorityService.hasRole(AuthorityService.TEACHER)){
            Long deptId=authorityService.getSysUser().getDeptId();
            wrapper.eq("d.id",deptId);
        }else if (authorityService.hasRole(AuthorityService.STUDENT)){

        }
        else{
            throw new Exception("权限不足");
        }
    }
}
