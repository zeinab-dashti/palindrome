package com.zeinab.palindrome.repository;

import com.zeinab.palindrome.entity.SubmissionEntity;
import com.zeinab.palindrome.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<SubmissionEntity, Long> {

    List<SubmissionEntity> findByUserOrderByScoreDesc(UserEntity user);
}
