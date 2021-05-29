package com.zeinab.palindrome.dto;

import lombok.Data;

@Data
public class TokenDTO {
    private String bearerToken;
    private long jwtExpirationDateInMilliseconds;
}
