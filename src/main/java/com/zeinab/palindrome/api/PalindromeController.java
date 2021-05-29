package com.zeinab.palindrome.api;

import com.zeinab.palindrome.dto.SubmissionDTO;
import com.zeinab.palindrome.dto.UserRegisterDTO;
import com.zeinab.palindrome.exception.InvalidWordException;
import com.zeinab.palindrome.service.SubmissionService;
import com.zeinab.palindrome.service.UserService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@Api(tags = "Endpoints for Palindrome game operations")
public class PalindromeController {

    private final SubmissionService submissionService;
    private final UserService userService;

    public PalindromeController(SubmissionService submissionService, UserService userService) {
        this.submissionService = submissionService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @ApiOperation(value = "Register a user")
    @ApiResponses({
            @ApiResponse(code = 409, message = "User with specified username already exists"),
            @ApiResponse(code = 200, message = "Register OK")
    })
    public ResponseEntity<?> register(@ApiParam(value = "User data object", required = true)
                                      @RequestBody UserRegisterDTO userRegisterDTO) {
        String output = userService.register(userRegisterDTO);
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    @PostMapping("/submit")
    @ApiOperation(value = "Submit a word")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid input word"),
            @ApiResponse(code = 200, message = "Submit a word done successfully")
    })
    public ResponseEntity<SubmissionDTO> submit(
            @ApiParam(value = "Authorization token")
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "Submission Word Object", required = true)
            @RequestBody SubmissionDTO submissionDTO) {

        String word = submissionDTO.getWord();
        if (!Pattern.compile("[a-zA-Z]+").matcher(word).matches())
            throw new InvalidWordException("Word invalid");
        SubmissionDTO response = submissionService.saveSubmission(token, word);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/leaderboard/me")
    @ApiOperation(value = "Get submissions of a user (read from token) sorted by score")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get submissions of a user done successfully")
    })
    public ResponseEntity<List<SubmissionDTO>> leaderBoard(
            @ApiParam(value = "Authorization token")
            @RequestHeader("Authorization") String token) {
        List<SubmissionDTO> submissions = submissionService.getLeaderBoardByUser(token);
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    @GetMapping("/leaderboard")
    @ApiOperation(value = "Get top 10 submissions of all users sorted by score")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get top 10 submissions of all users done successfully")
    })
    public ResponseEntity<List<SubmissionDTO>> leaderBoardAll() {
        List<SubmissionDTO> submissions = submissionService.getLeaderBoardAll();
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    @GetMapping("/status")
    @ApiOperation(value = "Health check")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok().body("Palindrom board is up!");
    }
}
