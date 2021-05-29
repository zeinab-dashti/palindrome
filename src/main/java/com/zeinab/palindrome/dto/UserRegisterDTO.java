package com.zeinab.palindrome.dto;

import com.zeinab.palindrome.entity.Role;
import com.zeinab.palindrome.entity.UserEntity;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class UserRegisterDTO {

    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserEntity toUser() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserEntity user = new UserEntity();
        user.setPassword(bCryptPasswordEncoder.encode(this.password));
        user.setUsername(this.username);
        user.setRole(this.role);
        String aRandomRefreshToken = RandomStringUtils.random(255, true, true);
        user.setRefreshToken(aRandomRefreshToken);
        return user;
    }
}
