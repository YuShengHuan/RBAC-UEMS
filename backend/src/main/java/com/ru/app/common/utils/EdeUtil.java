package com.ru.app.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EdeUtil {
    public static String passwordEncrypt(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }
    public static Boolean passwordMatch(String password, String passwordEncrypt) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(password, passwordEncrypt);
    }
}
