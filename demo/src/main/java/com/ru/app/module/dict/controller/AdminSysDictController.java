package com.ru.app.module.dict.controller;

import com.ru.app.module.dict.dto.AdminSysDictQueryDTO;
import com.ru.app.module.dict.service.AdminSysDictService;
import com.ru.app.common.entity.SysDict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/dict")
public class AdminSysDictController{
    private final AdminSysDictService adminSysDictService;

    public AdminSysDictController(AdminSysDictService adminSysDictService) {
        this.adminSysDictService=adminSysDictService;
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminSysDictQueryDTO queryDTO){
        return adminSysDictService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminSysDictService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SysDict sysDict){
        return adminSysDictService.create(sysDict);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody SysDict sysDict){
        return adminSysDictService.update(sysDict);
    }
    @PostMapping("/batch-insert")
    public ResponseEntity<?> batchInset(@RequestBody List<SysDict> sysDictList){
        return adminSysDictService.batchInsert(sysDictList);
    }
    @DeleteMapping("/batch-delete")
    public ResponseEntity<?> delete(@RequestBody List<Long> ids){
        return adminSysDictService.batchDelete(ids);
    }
}