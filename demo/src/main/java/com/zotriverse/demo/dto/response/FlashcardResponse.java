package com.zotriverse.demo.dto.response;

import com.zotriverse.demo.pojo.Lesson;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlashcardResponse {
    int id;
    String word;
    String definition;
    String image;
    Date createdAt;
    Date updateAt;
    Lesson lessonId;
//    Set<FlashcardStudy> flashcardStudySet;
//    Set<Quizhistory> quizhistorySet;
}
