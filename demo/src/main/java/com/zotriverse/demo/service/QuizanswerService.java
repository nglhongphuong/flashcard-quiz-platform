package com.zotriverse.demo.service;

import com.zotriverse.demo.dto.request.QuizanswerRequest;
import com.zotriverse.demo.dto.response.QuizanswerResponse;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.mapper.QuizanswerMapper;
import com.zotriverse.demo.pojo.Quizanswer;
import com.zotriverse.demo.pojo.Quizhistory;
import com.zotriverse.demo.repository.QuizanswerRepository;
import com.zotriverse.demo.repository.QuizhistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuizanswerService {
    @Autowired
    private QuizanswerRepository quizanswerRepository;

    @Autowired
    private QuizhistoryRepository quizhistoryRepository;

    @Autowired
    private QuizanswerMapper quizanswerMapper;

    // CREATE
    public QuizanswerResponse create(QuizanswerRequest request) {
        Quizhistory history = quizhistoryRepository.findById(request.getHistoryId().getId())
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

        Quizanswer answer = Quizanswer.builder()
                .definition(request.getDefinition())
                .position(request.getPosition())
                .historyId(history)
                .build();

        Quizanswer saved = quizanswerRepository.save(answer);

        return QuizanswerResponse.builder()
                .id(saved.getId())
                .definition(saved.getDefinition())
                .position(saved.getPosition())
                .build();
    }


    // UPDATE
    public QuizanswerResponse update(int id, QuizanswerRequest request) {
        Quizanswer existing = quizanswerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

        if (request.getHistoryId() != null) {
            Quizhistory history = quizhistoryRepository.findById(request.getHistoryId().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
            existing.setHistoryId(history);
        }

        existing.setDefinition(request.getDefinition());
        existing.setPosition(request.getPosition());

        quizanswerRepository.save(existing);
        return quizanswerMapper.toQuizanswerResponse(existing);
    }

    // DELETE
    public void delete(int id) {
        if (!quizanswerRepository.existsById(id)) {
            throw new AppException(ErrorCode.CANNOT_FOUND);
        }
        quizanswerRepository.deleteById(id);
    }
}
