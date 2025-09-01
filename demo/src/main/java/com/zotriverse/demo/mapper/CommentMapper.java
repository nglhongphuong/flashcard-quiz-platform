package com.zotriverse.demo.mapper;

import com.zotriverse.demo.dto.response.CommentResponse;
import com.zotriverse.demo.dto.response.UserInfoResponse;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface CommentMapper {
    default CommentResponse toCommentResponse(Comment comment) {
        if (comment == null) return null;

        Account account = comment.getUserId().getAccount(); // 👈 lấy từ userId
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .lessonId(comment.getLessonId())
                .userInfo(toUserInfo(account))
                .createdAt(comment.getCreatedAt())
                .updateAt(comment.getUpdateAt())
                .build();
    }
    default UserInfoResponse toUserInfo(Account account) {
        if (account == null) return null;
        return UserInfoResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .name(account.getName())   // name trong User
                .avatar(account.getAvatar()) // avatar trong User
                .role(account.getRole())
                .build();
    }
}
