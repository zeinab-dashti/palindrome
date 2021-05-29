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
import org.junit.jupiter.api.AfterEach;
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
public class SubmissionRestAssuredTest {

    @LocalServerPort
    private int port;
    private RequestSpecification userOneSpecification, userTwoSpecification;
    private SubmissionDTO submission;
    Set<String> PalindromSetOne, PalindromSetTwo;

    private static final UserRegisterDTO userOneDTO = new UserRegisterDTOBuilder()
            .setProperties("bob", "123", Role.PLAYER)
            .build();
    private static final UserRegisterDTO userTwoDTO = new UserRegisterDTOBuilder()
            .setProperties("john", "123", Role.PLAYER)
            .build();

    @BeforeEach
    public void setUp() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.port = port;

        registerUser(port, userOneDTO);
        String tokenOne = getToken(port, userOneDTO);
        userOneSpecification = buildSpecification(port, tokenOne);

        registerUser(port, userTwoDTO);
        String tokenTwo = getToken(port, userTwoDTO);
        userTwoSpecification = buildSpecification(port, tokenTwo);

        PalindromSetOne = getPalindromicSubstrings(RandomStringUtils.random(2, true, false));
        PalindromSetTwo = getPalindromicSubstrings(RandomStringUtils.random(5, true, false));
    }

    @Test
    void whenGetStatus_thenReturns200(){
        RestAssured.defaultParser = Parser.JSON;
         given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get(STATUS_URL)
                        .then()
                        .statusCode(HttpStatus.OK.value());
    }

    @Test
    void whenPostSubmission_thenReturns201(){
        RestAssured.defaultParser = Parser.JSON;
        submission = new SubmissionDTOBuilder()
                .setProperties("test")
                .build();

        Response response =
                given()
                .spec(userOneSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(submission)
                .post(SUBMIT_URL)
                .then()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().response();

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    void whenPostSubmission_givenInValidWord_thenReturns400(){
        RestAssured.defaultParser = Parser.JSON;

        submission = new SubmissionDTOBuilder()
                .setProperties("SOme InVaa111lid worD")
                .build();

        Response response =
                given()
                        .spec(userOneSpecification)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .body(submission)
                        .post(SUBMIT_URL)
                        .then()
                        .extract().response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void whenLeaderBoard_thenReturnsSpecificItems(){
        RestAssured.defaultParser = Parser.JSON;

        addSubmission(userOneSpecification, PalindromSetOne);
        addSubmission(userTwoSpecification, PalindromSetTwo);

        SubmissionDTO[] response =
                given()
                        .spec(userOneSpecification)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get(LEADERBOARD_URL)
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(SubmissionDTO[].class);

        Assertions.assertEquals(response.length, PalindromSetOne.size());
    }

    @Test
    void whenLeaderBoard_thenReturnsDesOrderedItems(){
        RestAssured.defaultParser = Parser.JSON;

        addSubmission(userOneSpecification, PalindromSetOne);
        addSubmission(userTwoSpecification, PalindromSetTwo);

        SubmissionDTO[] response =
                given()
                .spec(userOneSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(LEADERBOARD_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(SubmissionDTO[].class);

        SubmissionDTO topByScore = Arrays.stream(response)
                .max(Comparator.comparing(SubmissionDTO::getScore))
                .orElseThrow(NoSuchElementException::new);

        Assertions.assertEquals(response[0], topByScore);
    }

    @Test
    void whenLeaderBoardAll_thenReturnsTop10(){
        RestAssured.defaultParser = Parser.JSON;
        SubmissionDTO[] submissions1 = new SubmissionDTO[]{
                new SubmissionDTO("Bib", 2),
                new SubmissionDTO("Rotator", 4),
                new SubmissionDTO("Madam", 3),
                new SubmissionDTO("Civic", 3),
                new SubmissionDTO("z", 1),
                new SubmissionDTO("Hannah", 3),
                new SubmissionDTO("Level", 3)
        };
        SubmissionDTO[] submissions2 = new SubmissionDTO[]{
                new SubmissionDTO("Minim", 3),
                new SubmissionDTO("Mom", 2),
                new SubmissionDTO("N", 1),
                new SubmissionDTO("Radar", 3),
                new SubmissionDTO("c", 1),
                new SubmissionDTO("Sagas", 3)
        };

        for (SubmissionDTO submission: submissions1)
        {
                    given()
                            .spec(userOneSpecification)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .body(submission)
                            .post(SUBMIT_URL)
                            .then()
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .extract().response();
        }
        for (SubmissionDTO submission: submissions2)
        {
                    given()
                            .spec(userOneSpecification)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .body(submission)
                            .post(SUBMIT_URL)
                            .then()
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .extract().response();
        }

        SubmissionDTO[] response =
                given()
                .spec(userOneSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(LEADERBOARD_ALL_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(SubmissionDTO[].class);
        SubmissionDTO topByScore = Arrays.stream(response)
                .max(Comparator.comparing(SubmissionDTO::getScore))
                .orElseThrow(NoSuchElementException::new);

        Assertions.assertEquals(10, response.length);
        Assertions.assertEquals(4, response[0].getScore());
        Assertions.assertEquals("Rotator", response[0].getWord());
        Assertions.assertEquals(2, response[9].getScore());
    }
}
