package com.zotriverse.demo.mapper;

import com.zotriverse.demo.dto.request.LessonRequest;
import com.zotriverse.demo.dto.response.LessonResponse;
import com.zotriverse.demo.dto.response.UserInfoResponse;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Lesson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    Lesson toLesson(LessonRequest request);


    default LessonResponse toLessonResponse(Lesson lesson) {
        if (lesson == null) return null;
        Account account = lesson.getUserId() != null ? lesson.getUserId().getAccount() : null;
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .image(lesson.getImage())
                .visibility(lesson.getVisibility())
                .isCommentLocked(lesson.getIsCommentLocked())
                .createdAt(lesson.getCreatedAt())
                .updateAt(lesson.getUpdateAt())
                .flashcardSet(lesson.getFlashcardSet())
                .userInfo(toUserInfo(account))
                .build();
    }

    default UserInfoResponse toUserInfo(Account account) {
        if (account == null) return null;
        return UserInfoResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .name(account.getName())
                .avatar(account.getAvatar())
                .role(account.getRole())
                .build();
    }
}
