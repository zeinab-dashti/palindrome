package com.zeinab.palindrome.util;

import com.auth0.jwt.JWT;
import com.zeinab.palindrome.entity.Role;

import java.util.Date;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.zeinab.palindrome.security.SecurityConstants.*;

public class AuthUtil {

    public static String getJWTForNextHour(String username, Role role) {

        String token = JWT.create()
                .withClaim("username", username)
                .withClaim("role", role.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        return TOKEN_PREFIX + token;
    }
}
