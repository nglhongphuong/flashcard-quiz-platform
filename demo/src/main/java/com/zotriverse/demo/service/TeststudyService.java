package com.zotriverse.demo.service;

import com.zotriverse.demo.dto.response.QuizstudyInfoResponse;
import com.zotriverse.demo.enums.Result;
import com.zotriverse.demo.pojo.Quizhistory;
import com.zotriverse.demo.pojo.Quizstudy;
import com.zotriverse.demo.repository.TeststudyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeststudyService {
    @Autowired
    private TeststudyRepository teststudyRepository;

    public List<QuizstudyInfoResponse> getTestStudiesOnly(Map<String, String> params) {
        int lessonId = Integer.parseInt(params.getOrDefault("lessonId", "0"));
        int userId = Integer.parseInt(params.getOrDefault("userId", "0"));
        List<Quizstudy> testQuizList = teststudyRepository.findAllTeststudyByLessonId(lessonId, userId);

        return testQuizList.stream().map(quizstudy -> {
            Map<String, Integer> resultCounts = new HashMap<>();
            resultCounts.put(Result.CORRECT.getDisplayName(), 0);
            resultCounts.put(Result.INCORRECT.getDisplayName(), 0);
            resultCounts.put(Result.UNRESULT.getDisplayName(), 0);

            for (Quizhistory history : quizstudy.getQuizhistorySet()) {
                resultCounts.computeIfPresent(history.getResult(), (k, v) -> v + 1);
            }

            return QuizstudyInfoResponse.builder()
                    .id(quizstudy.getId())
                    .createdAt(quizstudy.getCreatedAt())
                    .updateAt(quizstudy.getUpdateAt())
                    .lessonId(quizstudy.getLessonId())
                    .userId(quizstudy.getUserId().getAccountId())
                    .results(resultCounts)
                    .build();
        }).collect(Collectors.toList());
    }


}
