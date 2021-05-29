package com.zeinab.palindrome.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.zeinab.palindrome.dto.UserRegisterDTO;
import com.zeinab.palindrome.entity.UserEntity;
import com.zeinab.palindrome.exception.ConflictUserException;
import com.zeinab.palindrome.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.zeinab.palindrome.security.SecurityConstants.SECRET;
import static com.zeinab.palindrome.security.SecurityConstants.TOKEN_PREFIX;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> getUserFromToken(String jwtToken) {
        Map<String, Claim> claims = JWT.require(HMAC512(SECRET.getBytes()))
                .build()
                .verify(jwtToken.replace(TOKEN_PREFIX, ""))
                .getClaims();

        String username = claims.get("username").asString();
        return userRepository.findByUsername(username);
    }

    public String register(UserRegisterDTO userRegisterDTO) {
        UserEntity userNameExists = userRepository.findByUsername(userRegisterDTO.getUsername())
                .orElse(null);
        if (userNameExists != null)
            throw new ConflictUserException("user already exists");

        userRepository.save(userRegisterDTO.toUser());
        return "OK";
    }
}
