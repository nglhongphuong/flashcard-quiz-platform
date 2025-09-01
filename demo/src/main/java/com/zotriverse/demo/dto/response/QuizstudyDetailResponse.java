package com.zotriverse.demo.dto.response;

import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.Quizhistory;
import com.zotriverse.demo.pojo.Teststudy;
import com.zotriverse.demo.pojo.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizstudyDetailResponse {
    Integer id;
    Lesson lessonId;
    User userId;
    Map<String, Integer> results;
    Set<QuizhistoryResponse> quizhistorySet;
    Teststudy teststudy;
    Date createdAt;
    Date updateAt;
}
