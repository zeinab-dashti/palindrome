package com.zeinab.palindrome.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Data
public class UserEntity {

    @Id
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonIgnore
    private String refreshToken;
}