package com.zeinab.palindrome.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class WordService {

    public int scoreSubmission(String word) {
        word = word.toLowerCase();
        if (isPalindrome(word))
            return getDistintCharsSize(word);
        else
            return 0;
    }

    private int getDistintCharsSize(String word) {
        Set charSet = new HashSet();
        for (int i = 0; i < word.length(); i++)
            charSet.add(word.charAt(i));
        return charSet.size();
    }

    private boolean isPalindrome(String word) {

        StringBuilder reverse = new StringBuilder();
        String toLowerCase = word.toLowerCase();
        char[] plain = toLowerCase.toCharArray();
        for (int i = plain.length - 1; i >= 0; i--) {
            reverse.append(plain[i]);
        }
        return reverse.toString().equals(toLowerCase);
    }
}
