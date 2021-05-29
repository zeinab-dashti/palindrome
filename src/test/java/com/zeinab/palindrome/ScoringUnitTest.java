package com.zeinab.palindrome;

import com.zeinab.palindrome.service.WordService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScoringUnitTest {

    @InjectMocks
    WordService wordService;

    @Test
    public void whenGetScore_thenReturnCorrectScore() {

        Assertions.assertEquals(0, wordService.scoreSubmission("abc"));
        Assertions.assertEquals(4, wordService.scoreSubmission("Rotator"));
        Assertions.assertEquals(3, wordService.scoreSubmission("Tenet"));
        Assertions.assertEquals(2, wordService.scoreSubmission("Wow"));
        Assertions.assertEquals(1, wordService.scoreSubmission("X"));
    }
}
