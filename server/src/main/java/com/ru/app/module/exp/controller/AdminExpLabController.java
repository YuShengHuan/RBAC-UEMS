package com.ru.app.module.exp.controller;
import com.ru.app.common.entity.SysDict;
import com.ru.app.module.exp.dto.AdminExpLabQueryDTO;
import com.ru.app.module.exp.service.AdminExpLabService;
import com.ru.app.common.entity.ExpLab;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/lab")
public class AdminExpLabController{
    private final AdminExpLabService adminExpLabService;

    public AdminExpLabController(AdminExpLabService adminExpLabService) {
        this.adminExpLabService=adminExpLabService;
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(){
        return adminExpLabService.selectOption();
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminExpLabQueryDTO queryDTO){
        return adminExpLabService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminExpLabService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ExpLab expLab){
        return adminExpLabService.create(expLab);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ExpLab expLab){
        return adminExpLabService.update(expLab);
    }
    @PostMapping("/batch-insert")
    public ResponseEntity<?> batchInset(@RequestBody List<ExpLab> expLabList){
        return adminExpLabService.batchInsert(expLabList);
    }
    @DeleteMapping("/batch-delete")
    public ResponseEntity<?> delete(@RequestBody List<Long> ids){
        return adminExpLabService.batchDelete(ids);
    }
}
