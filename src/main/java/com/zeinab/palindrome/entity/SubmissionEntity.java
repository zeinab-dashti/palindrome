package com.zeinab.palindrome.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
public class SubmissionEntity {

    @Id
    @GeneratedValue
    Long Id;
    Long datetime;
    @OneToOne
    UserEntity user;
    String word;
    Integer score;

    public SubmissionEntity(String word, Integer score, UserEntity user, Long datetime) {
        this.word = word;
        this.score = score;
        this.user = user;
        this.datetime = datetime;
    }
}
