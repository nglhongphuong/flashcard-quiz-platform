package com.zotriverse.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.Quizhistory;
import com.zotriverse.demo.pojo.Teststudy;
import com.zotriverse.demo.pojo.User;
import jakarta.persistence.*;
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
public class QuizstudyResponse {
    Integer id;
    Date createdAt;
    Date updateAt;
    Lesson lessonId;
    User userId;
    Set<Quizhistory> quizhistorySet;
    Teststudy teststudy;
}
