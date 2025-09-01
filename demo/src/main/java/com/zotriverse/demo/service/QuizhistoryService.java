package com.zotriverse.demo.service;

import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.pojo.Quizhistory;
import com.zotriverse.demo.repository.AccountRepository;
import com.zotriverse.demo.repository.QuizhistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class QuizhistoryService {
    @Autowired
    private QuizhistoryRepository quizhistoryRepository;

    @Autowired
    private AccountRepository accountRepository;
    public void deleteById(int id) {
        if (!quizhistoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.CANNOT_FOUND);
        }
        quizhistoryRepository.deleteById(id);
        log.info("Deleted QuizHistory with id: {}", id);
    }

    @Transactional
    public void resetQuizHistoryByQuizStudyId(Integer quizStudyId) {
        List<Quizhistory> histories = quizhistoryRepository.findByQuizStudyId(quizStudyId);
        for (Quizhistory history : histories) {
            history.setUserAnswer(null);
            history.setResult("UNRESULT");
        }
        quizhistoryRepository.saveAll(histories);
    }




}
