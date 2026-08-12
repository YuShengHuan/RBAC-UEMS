package com.ru.app.module.base.controller;

import com.ru.app.module.base.dto.AdminBaseDeptQueryDTO;
import com.ru.app.module.base.service.AdminBaseDeptService;
import com.ru.app.common.entity.BaseDept;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dept")
public class AdminBaseDeptController {
    private final AdminBaseDeptService adminBaseDeptService;

    public AdminBaseDeptController(AdminBaseDeptService adminBaseDeptService) {
        this.adminBaseDeptService=adminBaseDeptService;
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(){
        return adminBaseDeptService.selectOption();
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminBaseDeptQueryDTO queryDTO){
        return adminBaseDeptService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminBaseDeptService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody BaseDept baseDept){
        return adminBaseDeptService.create(baseDept);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody BaseDept baseDept){
        return adminBaseDeptService.update(baseDept);
    }
    @PostMapping("/batch-insert")
    public ResponseEntity<?> batchInset(@RequestBody List<BaseDept> baseDeptList){
        return adminBaseDeptService.batchInsert(baseDeptList);
    }
    @DeleteMapping("/batch-delete")
    public ResponseEntity<?> delete(@RequestBody List<Long> ids){
        return adminBaseDeptService.batchDelete(ids);
    }
}
