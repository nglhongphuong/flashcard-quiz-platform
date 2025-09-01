package com.zotriverse.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zotriverse.demo.pojo.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonResponse {

    int id;
    String title;
    String description;
    String image;
    String visibility;
    Boolean isCommentLocked;
    Date createdAt;
    Date updateAt;
    Set<Flashcard> flashcardSet;
    UserInfoResponse userInfo;//tuong tu userInfo

//    Set<Rating> ratingSet;
//    Set<Bookmark> bookmarkSet;
//    Set<Quizstudy> quizstudySet;
//    Set<Comment> commentSet;
//    Set<Viewhistory> viewhistorySet;
//    Set<Lessonschedule> lessonscheduleSet;
}
