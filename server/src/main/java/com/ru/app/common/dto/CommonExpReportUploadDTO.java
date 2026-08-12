package com.ru.app.common.dto;
import com.ru.app.common.entity.ExpReport;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CommonExpReportUploadDTO {
    private Long id;// '报告ID',
    private Long projectId;// '实验项目ID',
    private Long uploadUserId;// '上传用户ID',
    private String filePath;//文件路径
    private MultipartFile file;// '文件',
    private Integer attachmentType;// （1-实验模板/2-实验报告）
    private String remark;// '备注',

    public ExpReport toEntity(){
        ExpReport entity=new ExpReport();
        if(this.id!=null){
            entity.setId(this.id);
        }
        if(this.projectId!=null){
            entity.setProjectId(this.projectId);
        }
        if(this.uploadUserId!=null){
            entity.setUploadUserId(this.uploadUserId);
        }
        if(this.attachmentType!=null){
            entity.setAttachmentType(this.attachmentType);
        }
        if(StringUtils.hasText(this.remark)){
            entity.setRemark(this.remark);
        }
        if(StringUtils.hasText(this.filePath)){
            entity.setFilePath(this.filePath);
        }
        return entity;
    }
}
