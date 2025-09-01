package com.zotriverse.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor // constructor ko tham so
@AllArgsConstructor // constructor cos tham soo
@Builder
public class QuizstudyRequest {
    private String questionType; // e.g., "AUDIO", "TEXT_AUDIO"
    private List<String> answerTypes;    // e.g., "MULTIPLE_CHOICE", "TRUE_FALSE", "TEXT_INPUT", "FILL_IN_THE_BLANK"
    private String studyMode; // ENUM: NOT_REMEMBERED, NOT_LEARNED, REMEMBERED, RANDOM, CUSTOM
    // Áp dụng nếu chế độ học là RANDOM
    private Integer numberOfFlashcards;
    // Áp dụng nếu chế độ học là CUSTOM
    private List<Integer> flashcardIds;
}
