package com.zotriverse.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonscheduleResponse {
    Integer id;
    Date scheduledTime;
    String notice;
    Lesson lessonId;
    User userId;
    Date createdAt;
    Date updateAt;
}
