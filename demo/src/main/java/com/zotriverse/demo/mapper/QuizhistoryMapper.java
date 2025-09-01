package com.zotriverse.demo.mapper;

import com.zotriverse.demo.dto.response.QuizhistoryResponse;
import com.zotriverse.demo.pojo.Quizhistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuizhistoryMapper {
    QuizhistoryResponse toQuizhistoryResponse(Quizhistory quizhistory);
}
