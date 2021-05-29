package com.zeinab.palindrome.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeinab.palindrome.dto.TokenDTO;
import com.zeinab.palindrome.entity.Role;
import com.zeinab.palindrome.entity.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.zeinab.palindrome.security.SecurityConstants.EXPIRATION_TIME;
import static com.zeinab.palindrome.security.SecurityConstants.SECRET;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            UserEntity creds = new ObjectMapper().readValue(req.getInputStream(), UserEntity.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {

        User user = (User) auth.getPrincipal();

        long jwtExpirationDateInMilliseconds = (long) (System.currentTimeMillis() + EXPIRATION_TIME);
        String token = JWT.create()
                .withClaim("username", user.getUsername())
                .withClaim("role", retrieveRoleFromSpringCoreUser(user))
                .withExpiresAt(new Date(jwtExpirationDateInMilliseconds))
                .sign(HMAC512(SECRET.getBytes()));

        TokenDTO authTokenDTO = new TokenDTO();

        authTokenDTO.setBearerToken(token);
        authTokenDTO.setJwtExpirationDateInMilliseconds(jwtExpirationDateInMilliseconds);

        ObjectMapper mapper = new ObjectMapper();

        res.getWriter().write(mapper.writeValueAsString(authTokenDTO));
        res.getWriter().flush();
        res.getWriter().close();
    }

    private String retrieveRoleFromSpringCoreUser(User user) {
        Role role;
        try {
            role = Role.valueOf(user.getAuthorities().toArray()[0].toString());
        } catch (IllegalArgumentException e) {
            role = Role.valueOf(user.getAuthorities().toArray()[1].toString());
        }
        return role.name();
    }

}