package com.zeinab.palindrome;

import com.zeinab.palindrome.dto.SubmissionDTO;
import com.zeinab.palindrome.dto.UserRegisterDTO;
import com.zeinab.palindrome.dto.builder.SubmissionDTOBuilder;
import com.zeinab.palindrome.dto.builder.UserRegisterDTOBuilder;
import com.zeinab.palindrome.entity.Role;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.zeinab.palindrome.util.Constants.*;
import static com.zeinab.palindrome.util.GeneratePalindrom.getPalindromicSubstrings;
import static com.zeinab.palindrome.util.TestUtil.*;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRestAssuredTest {

    @LocalServerPort
    private int port;

    private static final UserRegisterDTO userDTO = new UserRegisterDTOBuilder()
            .setProperties("bob", "123", Role.PLAYER)
            .build();

    @BeforeEach
    public void setUp() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.port = port;

        registerUser(port, userDTO);
    }

    @Test
    void whenRegisterDuplicateUser_thenReturns409() {
        RestAssured.defaultParser = Parser.JSON;
        Response response =
                given()
                .basePath(REGISTER_URL)
                .port(port)
                .contentType("application/json")
                .body(userDTO)
                .when()
                .post()
                .then()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().response();
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode());
    }
}
