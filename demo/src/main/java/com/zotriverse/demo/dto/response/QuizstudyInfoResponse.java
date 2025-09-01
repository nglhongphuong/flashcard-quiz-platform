package com.zotriverse.demo.dto.response;

import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.Teststudy;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizstudyInfoResponse {
    Integer id;
    Date createdAt;
    Date updateAt;
    Lesson lessonId;
    Integer userId;
    Map<String, Integer> results; // CORRECT: 2, INCORRECT: 5, ...
    Teststudy teststudy;
}
