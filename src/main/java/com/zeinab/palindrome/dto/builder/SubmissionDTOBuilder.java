package com.zeinab.palindrome.dto.builder;

import com.zeinab.palindrome.dto.SubmissionDTO;
import lombok.Data;

@Data
public class SubmissionDTOBuilder {

    private final SubmissionDTO submission = new SubmissionDTO();

    public SubmissionDTOBuilder setProperties(String word) {
        submission.setWord(word);
        return this;
    }

    public SubmissionDTO build() {
        return submission;
    }
}
