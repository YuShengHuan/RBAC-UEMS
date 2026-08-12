package com.ru.app.common.interceptor;

import com.ru.app.common.response.TokenResponse;
import com.ru.app.common.service.TokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import java.io.IOException;
@Component

public class TokenInterceptor implements HandlerInterceptor {
    private final TokenService tokenService;
    public TokenInterceptor(
            TokenService tokenService
    ){
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            SendError(response, TokenResponse.ACCESS_TOKEN_ERROR.CONTENT);
            return false;
        }
        String jwt = token.substring(7);
        try {
            if (!tokenService.validateToken(jwt)) {
                SendError(response, TokenResponse.ACCESS_TOKEN_ERROR.CONTENT);
                return false;
            }
            // 将用户信息存入请求属性
            tokenService.setRequestAttributeByToken(request,jwt);

            return true;
        } catch (JwtException e) {
            if(e.getMessage().equals(TokenResponse.TOKEN_EXPIRED.CONTENT)){
                try{
                    tokenService.refreshNewAccessToken(request,response,jwt);
                    return true;
                }catch (JwtException exception){
                    SendError(response, TokenResponse.REFRESH_TOKEN_ERROR.CONTENT);
                }
            }else{
                SendError(response, TokenResponse.ACCESS_TOKEN_ERROR.CONTENT);
            }
            return false;
        }
    }
    void SendError(HttpServletResponse response,String error) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(error);
    }

}
