package com.ru.app.module.exp.controller;
import com.ru.app.module.exp.dto.AdminExpReportQueryDTO;
import com.ru.app.module.exp.dto.FrontExpReportQueryDTO;
import com.ru.app.module.exp.dto.FrontExpReportTemplateQueryDTO;
import com.ru.app.common.dto.CommonExpReportUploadDTO;
import com.ru.app.module.exp.service.AdminExpReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/report")
public class AdminExpReportController {
    private final AdminExpReportService adminExpReportService;

    public AdminExpReportController(AdminExpReportService adminExpReportService) {
        this.adminExpReportService=adminExpReportService;
    }
    @PostMapping("/page/submitted")
    public ResponseEntity<?> pageSubmitted(@RequestBody FrontExpReportQueryDTO queryDTO){
        return adminExpReportService.queryReportStatus(queryDTO,true);
    }
    @PostMapping("/page/submitted/download/zip")
    public ResponseEntity<?> submittedFilesToZip(HttpServletResponse response, @RequestBody FrontExpReportQueryDTO queryDTO){
        return adminExpReportService.exportReportSubmitToZip(response,queryDTO);
    }
    @PostMapping("/page/un-submitted")
    public ResponseEntity<?> pageUnSubmitted(@RequestBody FrontExpReportQueryDTO queryDTO){
        return adminExpReportService.queryReportStatus(queryDTO,false);
    }
    @PostMapping("/page/template")
    public ResponseEntity<?> pageTemplate(@RequestBody FrontExpReportTemplateQueryDTO queryDTO){
        return adminExpReportService.queryTemplate(queryDTO);
    }
    @GetMapping("/detail/{reportId}")
    public ResponseEntity<?> reportDetail(@PathVariable Long reportId){
        return adminExpReportService.queryDetail(reportId);
    }
    @GetMapping("/word-to-pdf/{reportId}")
    public ResponseEntity<?> convertWordToPdf(@PathVariable Long reportId) {
        return adminExpReportService.convertWordToPdf(reportId);
    }
    @GetMapping("/plagiarism/{reportId}")
    public ResponseEntity<?> plagiarism(@PathVariable Long reportId) {
        return adminExpReportService.plagiarism(reportId);
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(){
        return adminExpReportService.selectOption();
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminExpReportQueryDTO queryDTO){
        return adminExpReportService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminExpReportService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@ModelAttribute CommonExpReportUploadDTO eu){
        try {
            return adminExpReportService.create(eu);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@ModelAttribute CommonExpReportUploadDTO eu){
        try {
            return adminExpReportService.update(eu);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
}
