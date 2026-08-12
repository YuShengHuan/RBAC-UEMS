package com.ru.app.common.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ru.app.common.entity.SysUser;
import com.ru.app.common.utils.EdeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BaseDatabaseService<M extends BaseMapper<T>,T> extends ServiceImpl<M,T> {
    @Transactional
    public ResponseEntity<?> create(T entity){
        try {
            Assert.notNull(entity, "实体不能为空");
            boolean status=save(entity);
            if(status){
                return ResponseEntity.status(HttpStatus.CREATED).body("创建成功");
            }else{
                return ResponseEntity.badRequest().body("创建失败");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    @Transactional
    public ResponseEntity<?> update(T entity){
        try {
            Assert.notNull(entity, "实体不能为空");
            boolean status=updateById(entity);
            if(status){
                return ResponseEntity.ok().body("更新成功");
            }else{
                return ResponseEntity.badRequest().body("更新失败");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    @Transactional
    public ResponseEntity<?> delete(Serializable id){
        try {
            Assert.notNull(id, "删除失败：ID 不能为空");
            boolean status=this.removeById(id);
            if(status){
                return ResponseEntity.ok().body("删除成功");
            }else{
                return ResponseEntity.badRequest().body("删除失败");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    @Transactional
    public ResponseEntity<?> batchDelete(Collection<?> ids){
        try {
            Assert.notNull(ids, "删除失败：ID 不能为空");
            boolean status=this.removeBatchByIds(ids);
            if(status){
                return ResponseEntity.ok().body("删除成功");
            }else{
                return ResponseEntity.badRequest().body("删除失败");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
}
