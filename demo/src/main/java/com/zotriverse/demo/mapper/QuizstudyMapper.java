package com.zotriverse.demo.mapper;

import com.zotriverse.demo.dto.response.QuizstudyDetailResponse;
import com.zotriverse.demo.dto.response.QuizstudyResponse;
import com.zotriverse.demo.pojo.Quizstudy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuizstudyMapper {
    QuizstudyResponse toQuizstudyReponse(Quizstudy quizstudy);


}
