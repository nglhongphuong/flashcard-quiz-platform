package com.zotriverse.demo.mapper;

import com.zotriverse.demo.dto.request.LessonRequest;
import com.zotriverse.demo.dto.response.FlashcardResponse;
import com.zotriverse.demo.pojo.Flashcard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {
    Flashcard toFlashcard(LessonRequest request);
    FlashcardResponse toFlashcardResponse(Flashcard flashcard);
}
