package com.ru.app.common.service;


import com.ru.app.common.dto.SelfRolePermissionDTO;
import com.ru.app.common.response.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.util.*;

@Service
public class TokenService {

    private final RedisService redisService;

    TokenService(RedisService redisService){
        this.redisService=redisService;
    }

    public void setRefreshAndAccessToken(HttpServletResponse response, String userAccount){
        String accessToken= generateAccessToken(userAccount);
        String refreshToken= generateRefreshToken(userAccount);
        setRefreshTokenFromRedis(userAccount,refreshToken,getRefreshExpiration()/1000);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // 生产环境设为true
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int)(getRefreshExpiration() / 1000)); // 转为秒
        // 将刷新令牌存入HttpOnly Cookie
        response.addCookie(refreshTokenCookie);
       //将访问令牌存入Headers
        response.setHeader("authorization","Bearer "+accessToken);
    }
    private void setRefreshTokenFromRedis(String userAccount,String token,long time){
        redisService.set("refreshToken_"+userAccount,token, time);
    }
    private String getRefreshTokenFromRedis(String token){
        String username= getUserAccountFromToken(token);
        return (String) redisService.get("refreshToken_"+username);
    }
    private void delRefreshTokenFromRedis(String token){
        String username= getUserAccountFromToken(token);
        redisService.del("refreshToken_"+username);
    }
    public void setRequestAttributeByToken(HttpServletRequest request,String token){
        request.setAttribute("userAccount", getUserAccountFromToken(token));
    }
    String getRefreshTokenFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void refreshNewAccessToken(HttpServletRequest request,HttpServletResponse response,String accessToken) throws Exception {
        String redisRefreshToken=getRefreshTokenFromRedis(accessToken);
        String refreshToken=getRefreshTokenFromCookies(request);
        System.out.println("access-token："+accessToken);
        System.out.println("redis-refresh-token："+redisRefreshToken);
        System.out.println("refresh-token："+refreshToken);
        if(redisRefreshToken==null){
            throw new JwtException(TokenResponse.REFRESH_TOKEN_ERROR.CONTENT);
        }
        if(!redisRefreshToken.equals(refreshToken)){
            //cookie和redis的不一致的就删除
            delRefreshTokenFromRedis(refreshToken);
            throw new JwtException(TokenResponse.REFRESH_TOKEN_ERROR.CONTENT);
        }
        //刷新令牌
        if(validateToken(refreshToken)){
            String newAccessToken= refreshAccessToken(accessToken);
            response.setHeader("authorization","Bearer "+newAccessToken);
            // 将用户信息存入请求属性
            setRequestAttributeByToken(request,newAccessToken);
        }
    }

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration; // 访问令牌有效期，单位毫秒 24小时

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration; // 刷新令牌有效期，单位毫秒 7天


    // 生成密钥
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 生成访问令牌
    public String generateAccessToken(String userAccount) {
        return Jwts.builder()
                .subject(userAccount)
                .id(UUID.randomUUID().toString()) // 添加唯一ID
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 生成刷新令牌
    public String generateRefreshToken(String userAccount) {
        return Jwts.builder()
                .subject(userAccount)
                .id(UUID.randomUUID().toString()) // 添加唯一ID
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 验证JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException(TokenResponse.TOKEN_EXPIRED.CONTENT);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException(TokenResponse.TOKEN_ERROR.CONTENT);
        }
    }
    // 解析用户名
// 从令牌中获取用户名（支持过期令牌）
    public String getUserAccountFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            // 过期令牌仍可获取Claims
            return e.getClaims().getSubject();
        } catch (Exception e) {
            return null;
        }
    }
    // 从令牌中获取ID
    public String getIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }

    // 从令牌中获取发行时间
    public Date getIssuedAtFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getIssuedAt();
    }
    // 刷新访问令牌
    public String refreshAccessToken(String token) {
        return generateAccessToken(
                getUserAccountFromToken(token)
        );
    }
    public long getRefreshExpiration() {
        return refreshExpiration;
    }
    public long getAccessExpiration(){
        return expiration;
    }
}
