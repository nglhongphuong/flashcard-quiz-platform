package com.zotriverse.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zotriverse.demo.pojo.Quizhistory;
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
public class QuizanswerResponse {
    Integer id;
    String definition;
    int position;
    Date createdAt;
    Date updateAt;
    Quizhistory historyId;
}
