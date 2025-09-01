package com.zotriverse.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zotriverse.demo.pojo.Flashcard;
import com.zotriverse.demo.pojo.FlashcardStudyPK;
import com.zotriverse.demo.pojo.User;
import jakarta.persistence.*;
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
public class FlashcardStudyResponse {
    protected FlashcardStudyPK flashcardStudyPK;
   String status;
   Date createdAt;
   Date updateAt;
   //Moi quan he cua khoa ngoai hoi
   Flashcard flashcard;
   User user;
}
