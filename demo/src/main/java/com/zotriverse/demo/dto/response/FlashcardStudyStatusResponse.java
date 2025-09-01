package com.zotriverse.demo.dto.response;

import com.zotriverse.demo.pojo.Flashcard;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlashcardStudyStatusResponse {
    String status;
    int num;
    List<FlashcardResponse> flashcardSet;
}
