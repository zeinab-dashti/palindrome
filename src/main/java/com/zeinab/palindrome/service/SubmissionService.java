package com.zeinab.palindrome.service;

import com.zeinab.palindrome.dto.SubmissionDTO;
import com.zeinab.palindrome.entity.SubmissionEntity;
import com.zeinab.palindrome.entity.UserEntity;
import com.zeinab.palindrome.repository.SubmissionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    private final WordService wordService;
    private final UserService userService;
    private final SubmissionRepository submissionRepository;


    public SubmissionService(
            final WordService wordService,
            final UserService userService,
            final SubmissionRepository submissionRepository) {
        this.wordService = wordService;
        this.userService = userService;
        this.submissionRepository = submissionRepository;
    }

    public SubmissionDTO saveSubmission(String token, String word) {
        int score = wordService.scoreSubmission(word);
        UserEntity user = userService.getUserFromToken(token).get();
        SubmissionEntity submissionEntity = new SubmissionEntity(word, score, user, System.currentTimeMillis());
        submissionRepository.save(submissionEntity);
        return new SubmissionDTO(submissionEntity);
    }

    public List<SubmissionDTO> getLeaderBoardByUser(String token) {
        Optional<UserEntity> user = userService.getUserFromToken(token);
        List<SubmissionEntity> submissionEntities = submissionRepository.findByUserOrderByScoreDesc(user.get());
        return getSubmissionDTOS(submissionEntities);
    }

    public List<SubmissionDTO> getLeaderBoardAll() {
        List<SubmissionEntity> submissionEntities = submissionRepository
                .findAll(PageRequest.of(0, 10, Sort.by("score").descending()))
                .toList();
        return getSubmissionDTOS(submissionEntities);
    }

    private List<SubmissionDTO> getSubmissionDTOS(List<SubmissionEntity> submissionEntities) {
        return submissionEntities
                .stream()
                .map(SubmissionDTO::new)
                .collect(Collectors.toList());
    }

    public void deleteAllSubmission(String token) {
        submissionRepository.deleteAll();
    }
}
