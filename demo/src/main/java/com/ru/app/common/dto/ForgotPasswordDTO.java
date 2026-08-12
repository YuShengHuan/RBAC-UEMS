package com.ru.app.common.dto;

import lombok.Data;

@Data
public class ForgotPasswordDTO {
    private String userAccount;
    private String email;
    private String code;
    private String password;
    private Integer step;
}
