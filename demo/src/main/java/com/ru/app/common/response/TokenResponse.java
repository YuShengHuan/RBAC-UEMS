package com.ru.app.common.response;

import org.springframework.http.HttpStatus;

// 定义两种核心错误码
public enum TokenResponse {
    ACCESS_TOKEN_ERROR(HttpStatus.UNAUTHORIZED,"ACCESS_TOKEN_ERROR", "访问令牌错误"),
    REFRESH_TOKEN_ERROR(HttpStatus.UNAUTHORIZED,"REFRESH_TOKEN_ERROR", "刷新令牌错误"),
    TOKEN_ERROR(HttpStatus.UNAUTHORIZED,"TOKEN_ERROR"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,"TOKEN_EXPIRED");
    public final String CONTENT;
    public final HttpStatus STATUS;
    TokenResponse(HttpStatus status, String code, String message) {
        this.STATUS=status;
        this.CONTENT="{\"code\":\""+code+"\",\"message\":\""+message+"\"}";
    }
    TokenResponse(HttpStatus status, String content) {
        this.STATUS=status;
        this.CONTENT=content;
    }

}