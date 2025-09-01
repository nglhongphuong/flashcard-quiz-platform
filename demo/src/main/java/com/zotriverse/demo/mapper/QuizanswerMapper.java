package com.zotriverse.demo.mapper;

import com.zotriverse.demo.dto.response.QuizanswerResponse;
import com.zotriverse.demo.pojo.Quizanswer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuizanswerMapper {
    QuizanswerResponse toQuizanswerResponse(Quizanswer quizanswer);

}
