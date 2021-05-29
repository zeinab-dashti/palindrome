package com.zeinab.palindrome;

import com.zeinab.palindrome.api.PalindromeController;
import com.zeinab.palindrome.dto.UserRegisterDTO;
import com.zeinab.palindrome.dto.builder.UserRegisterDTOBuilder;
import com.zeinab.palindrome.entity.Role;
import com.zeinab.palindrome.entity.UserEntity;
import com.zeinab.palindrome.service.SubmissionService;
import com.zeinab.palindrome.service.UserDetailsServiceImpl;
import com.zeinab.palindrome.service.UserService;
import com.zeinab.palindrome.util.AuthUtil;
import com.zeinab.palindrome.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.zeinab.palindrome.security.SecurityConstants.HEADER_STRING;
import static com.zeinab.palindrome.util.TestUtil.toJSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PalindromeController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SubmissionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubmissionService submissionService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private BCryptPasswordEncoder encoder;

    static String validUserJWT;

    private static final UserRegisterDTO userDTO = new UserRegisterDTOBuilder()
            .setProperties("bob", "123", Role.PLAYER)
            .build();

    @BeforeEach
    public void setup() {
        validUserJWT = AuthUtil.getJWTForNextHour(
                userDTO.getUsername(), userDTO.getRole());
    }

    @Test
    void whenGetStatus_thenReturns200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Constants.STATUS_URL)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostRegister_thenReturns201() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(Constants.REGISTER_URL)
                .header(HEADER_STRING, validUserJWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJSON(userDTO)))
                .andExpect(status().isCreated());
    }
}
