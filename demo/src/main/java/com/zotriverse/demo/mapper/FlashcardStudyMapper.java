package com.zotriverse.demo.mapper;

import com.zotriverse.demo.dto.response.FlashcardResponse;
import com.zotriverse.demo.dto.response.FlashcardStudyResponse;
import com.zotriverse.demo.dto.response.FlashcardStudyStatusResponse;
import com.zotriverse.demo.pojo.Flashcard;
import com.zotriverse.demo.pojo.FlashcardStudy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface FlashcardStudyMapper {
//    @Mapping(target = "flashcard", ignore = true)
//    @Mapping(target = "user", ignore = true)
    FlashcardStudyResponse toFlashcardStudyResponse(FlashcardStudy flashcardStudy);



}
