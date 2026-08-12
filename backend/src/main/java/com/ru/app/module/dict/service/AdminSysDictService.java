package com.ru.app.module.dict.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysDictDTO;
import com.ru.app.common.dto.CommonSysRoleDTO;
import com.ru.app.common.entity.SysRole;
import com.ru.app.module.dict.dto.AdminSysDictQueryDTO;
import com.ru.app.module.dict.mapper.AdminSysDictMapper;
import com.ru.app.common.entity.SysDict;
import com.ru.app.common.service.BaseDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class AdminSysDictService extends BaseDatabaseService<AdminSysDictMapper,SysDict> {
    public ResponseEntity<?> queryPage(AdminSysDictQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonSysDictDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getDictGroup())){
                wrapper.like("dt.dict_group",queryDTO.getDictGroup());
            }
            if(StringUtils.hasText(queryDTO.getDictKey())){
                wrapper.like("dt.dict_kay",queryDTO.getDictKey());
            }
            if(StringUtils.hasText(queryDTO.getDictValue())){
                wrapper.like("dt.dict_value",queryDTO.getDictValue());
            }
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> batchInsert(List<SysDict> sysDictList) {
        try {
            //先获取所有权限
            QueryWrapper<SysDict> wrapper=new QueryWrapper<>();
            List<CommonSysDictDTO> commonSysPermissionDTOList=baseMapper.queryAll(wrapper);
            //以roleCode为主键，先排查存在的，不存在的放一边
            List<SysDict> noExitInDb=sysDictList.stream().filter(
                    item-> commonSysPermissionDTOList.stream().noneMatch(
                            w->w.getDictGroup().equals(item.getDictGroup())&&
                                    w.getDictKey().equals(item.getDictKey())&&
                                            w.getDictValue().equals(item.getDictValue())
                    )
            ).toList();
            List<SysDict> exitInDb=sysDictList.stream().filter(
                    item-> commonSysPermissionDTOList.stream().anyMatch(
                            w->w.getDictGroup().equals(item.getDictGroup())&&
                                    w.getDictKey().equals(item.getDictKey())&&
                                    w.getDictValue().equals(item.getDictValue())
                    )
            ).toList();
            System.out.println("不存在的数量："+noExitInDb.size());
            System.out.println("存在的数量："+exitInDb.size());

            boolean status= saveBatch(noExitInDb);
             if(status){
                 return ResponseEntity.ok().body("插入成功");
             }else{
                 return ResponseEntity.badRequest().body("插入失败");
             }
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }

}