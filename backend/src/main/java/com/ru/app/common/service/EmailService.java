package com.ru.app.common.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final RedisService redisService;
    @Value("${spring.mail.username}") // ✅ 正确注入配置
    private String emailFrom;
    @Value("${email.code.expiration}")
    private long emailCodeExpiration;
    private static final SecureRandom random = new SecureRandom();
    private final AuthorityService authorityService;

    EmailService(
            JavaMailSender mailSender,
            RedisService redisService,
            AuthorityService authorityService
    ){
        this.mailSender=mailSender;
        this.redisService=redisService;
        this.authorityService=authorityService;

    }
    public String getEmailInfoFromRedis(String userAccount){
        return (String) redisService.get("emilInfo_"+userAccount);
    }
    public void setEmailInfoToRedis(String userAccount, String email, String emailCode){
        redisService.set("emilInfo_"+userAccount,email+"_"+emailCode,getEmailCodeExpiration()/1000);
    }
    public void setEmailInfoToRedis(String userAccount, String email, String emailCode,boolean isSuccess){
        redisService.set("emilInfo_"+userAccount,email+"_"+emailCode+"_"+isSuccess,getEmailCodeExpiration()/1000);
    }
    public void delEmailInfoFromRedis(String userAccount){
        redisService.del("emilInfo_"+userAccount);
    }
    // 生成随机验证码
    public static String generate(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 生成纯数字验证码
        }
        return sb.toString();
    }
    public long getEmailCodeExpiration(){
        return this.emailCodeExpiration;
    }
    public void send(String email) throws MessagingException {
         String code=sendVerificationCode(email);
         String userAccount= authorityService.getUserAccount();
         if(getEmailInfoFromRedis(userAccount)!=null){
             delEmailInfoFromRedis(userAccount);
         }
         setEmailInfoToRedis(userAccount,email,code);
    }
    public void send(String userAccount,String email, boolean isSuccess) throws MessagingException {
        String code=sendVerificationCode(email);
        if(getEmailInfoFromRedis(userAccount)!=null){
            delEmailInfoFromRedis(userAccount);
        }
        setEmailInfoToRedis(userAccount,email,code,isSuccess);
    }
    // 发送验证码邮件
    private String sendVerificationCode(String email) throws MessagingException {
        String code = generate(6);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(emailFrom);
        helper.setTo(email);
        helper.setSubject("【实验管理系统】邮箱验证码");

        // 使用HTML模板
        String htmlContent = "<div style=\"border:1px solid #eee; padding:20px;\">\n    <h2 style=\"color: #333;\">您的验证码：</h2>\n    <p style=\"font-size: 24px; color: #1890ff;\">" + code + "</p>\n    <p style=\"color: #999;\">有效期3分钟，请勿泄露给他人</p>\n</div>\n";

        helper.setText(htmlContent, true);
        mailSender.send(message);
        return code;
    }
    /**
     * 邮箱脱敏：保留本地部分前2位+最后1位，中间用***替换，域名完整保留
     * @param email 原始邮箱
     * @return 脱敏后的邮箱，若输入无效则返回null
     */
    public  String desensitizeEmail(String email) {
        // 1. 校验输入合法性（非空、包含@、@后有内容）
        if (email == null || !email.contains("@") || email.split("@").length != 2) {
            return null; // 或返回原始邮箱，根据业务需求调整
        }
        // 2. 拆分本地部分和域名部分
        String[] parts = email.split("@");
        String localPart = parts[0]; // 本地部分（如 example123）
        String domain = parts[1];   // 域名部分（如 qq.com）

        // 3. 处理本地部分脱敏
        String desensitizedLocal;
        if (localPart.length() <= 2) {
            // 本地部分长度≤2，无需脱敏
            desensitizedLocal = localPart;
        } else {
            // 保留前2位 + *** + 最后1位
            desensitizedLocal = localPart.substring(0, 2)
                    + "***"
                    + localPart.substring(localPart.length() - 1);
        }
        // 4. 拼接脱敏后的邮箱
        return desensitizedLocal + "@" + domain;
    }
}

