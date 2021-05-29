package com.zeinab.palindrome.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeinab.palindrome.dto.SubmissionDTO;
import com.zeinab.palindrome.dto.TokenDTO;
import com.zeinab.palindrome.dto.UserRegisterDTO;
import com.zeinab.palindrome.dto.builder.SubmissionDTOBuilder;
import com.zeinab.palindrome.security.SecurityConstants;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Set;

import static com.zeinab.palindrome.security.SecurityConstants.TOKEN_PREFIX;
import static com.zeinab.palindrome.util.Constants.*;
import static io.restassured.RestAssured.given;

public class TestUtil {

    public static String toJSON(Object object) throws JsonProcessingException {
        return new ObjectMapper()
                .writer()
                .withDefaultPrettyPrinter()
                .writeValueAsString(object);
    }

    public static void registerUser(int port, UserRegisterDTO userDTO) {
        given()
                .basePath(REGISTER_URL)
                .port(port)
                .contentType("application/json")
                .body(userDTO)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    public static String getToken(int port, UserRegisterDTO userDTO) {
        return given()
                .basePath(LOGIN_URL)
                .port(port)
                .contentType("application/json")
                .body(userDTO)
                .when()
                .post()
                .then()
                .extract()
                .as(TokenDTO.class).getBearerToken();
    }

    public static RequestSpecification buildSpecification(int port, String token) {
        return new RequestSpecBuilder()
                .addHeader(SecurityConstants.HEADER_STRING, TOKEN_PREFIX + token)
                .setPort(port)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    public static void addSubmission(RequestSpecification specification, Set<String> palindrams) {
        for (String item: palindrams) {
            SubmissionDTO submission = new SubmissionDTOBuilder()
                    .setProperties(item)
                    .build();

            given()
                    .spec(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .body(submission)
                    .post(SUBMIT_URL)
                    .then()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .extract().response();
        }
    }
}
