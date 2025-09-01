package com.zotriverse.demo.mapper;

import com.zotriverse.demo.dto.response.LessonscheduleResponse;
import com.zotriverse.demo.pojo.Lessonschedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface LessonscheduleMapper {
    LessonscheduleResponse toLessonscheduleResponse(Lessonschedule lessonschedule);
}
