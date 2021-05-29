package com.zeinab.palindrome.dto.builder;

import com.zeinab.palindrome.dto.UserRegisterDTO;
import com.zeinab.palindrome.entity.Role;
import lombok.Data;

@Data
public class UserRegisterDTOBuilder {

    private final UserRegisterDTO userInputDTO = new UserRegisterDTO();

    public UserRegisterDTOBuilder setProperties(String userName, String password, Role role) {
        userInputDTO.setUsername(userName);
        userInputDTO.setPassword(password);
        userInputDTO.setRole(role);

        return this;
    }

    public UserRegisterDTO build() {
        return userInputDTO;
    }
}
