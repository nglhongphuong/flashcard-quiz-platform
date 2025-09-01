package com.zotriverse.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zotriverse.demo.pojo.Quizstudy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor // constructor ko tham so
@AllArgsConstructor // constructor cos tham soo
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeststudyRequest {
    int min;
    QuizstudyRequest quizstudy;

}
