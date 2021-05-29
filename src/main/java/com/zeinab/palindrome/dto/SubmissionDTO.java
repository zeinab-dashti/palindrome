package com.zeinab.palindrome.dto;

import com.zeinab.palindrome.entity.SubmissionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDTO {

    private Long datetime;
    private String username;
    private String word;
    private int score;

    public SubmissionDTO(SubmissionEntity submissionEntity) {
        this.datetime = submissionEntity.getDatetime();
        this.username = submissionEntity.getUser().getUsername();
        this.word = submissionEntity.getWord();
        this.score = submissionEntity.getScore();
    }

    public SubmissionDTO(String word, int score) {
        this.word = word;
        this.score = score;
    }
}
