package com.zotriverse.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zotriverse.demo.pojo.Flashcard;
import com.zotriverse.demo.pojo.Quizanswer;
import com.zotriverse.demo.pojo.Quizstudy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizhistoryResponse {
    Integer id;
    Flashcard flashcardId;
    String explanation;
    String questionType;
    String answerType;
    String result;
    String userAnswer;
//    Quizstudy quizId;
    Set<QuizanswerResponse> quizanswerSet;
    Date createdAt;
    Date updateAt;
}
