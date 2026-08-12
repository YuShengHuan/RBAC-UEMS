package com.ru.app.module.exp.controller;
import com.ru.app.module.exp.dto.AdminExpReportReviewQueryDTO;
import com.ru.app.module.exp.dto.FrontExpReportReviewQueryDTO;
import com.ru.app.module.exp.service.AdminExpReportReviewService;
import com.ru.app.common.entity.ExpReportReview;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/report-review")
public class AdminExpReportReviewController {
    private final AdminExpReportReviewService adminExpReportReviewService;

    public AdminExpReportReviewController(AdminExpReportReviewService adminExpReportReviewService) {
        this.adminExpReportReviewService=adminExpReportReviewService;
    }
    @PostMapping("/page/reviewed")
    public ResponseEntity<?> pageReviewed(@RequestBody FrontExpReportReviewQueryDTO queryDTO){
        return adminExpReportReviewService.queryReportReviewStatusPage(queryDTO,true);
    }
    @PostMapping("/page/un-reviewed")
    public ResponseEntity<?> pageUnReviewed(@RequestBody FrontExpReportReviewQueryDTO queryDTO){
        return adminExpReportReviewService.queryReportReviewStatusPage(queryDTO,false);
    }
    @GetMapping("/detail/{reviewId}")
    public ResponseEntity<?> detail(@PathVariable Long reviewId){
        return adminExpReportReviewService.queryDetail(reviewId);
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminExpReportReviewQueryDTO queryDTO){
        return adminExpReportReviewService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminExpReportReviewService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ExpReportReview expReportReview){
        return adminExpReportReviewService.create(expReportReview);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ExpReportReview expReportReview){
       return adminExpReportReviewService.update(expReportReview);
    }
}
