package com.zotriverse.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zotriverse.demo.pojo.FlashcardStudy;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.Quizhistory;
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
@NoArgsConstructor // constructor ko tham so
@AllArgsConstructor // constructor cos tham soo
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlashcardRequest {

    String word;
    String definition;
    String image;
//    Lesson lessonId;

}
