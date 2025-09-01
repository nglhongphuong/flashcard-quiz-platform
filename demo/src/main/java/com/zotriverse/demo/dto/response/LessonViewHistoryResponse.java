package com.zotriverse.demo.dto.response;

import com.zotriverse.demo.pojo.Flashcard;
import com.zotriverse.demo.pojo.Lessonschedule;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonViewHistoryResponse {
    int id;
    String title;
    String description;
    String image;
    String visibility;
    Boolean isCommentLocked;
    Date createdAt;
    Date updateAt;
    Set<Flashcard> flashcardSet;
    UserInfoResponse userInfo;//tac gia tao

    ViewhistoryResponse viewhistoryResponse;// cua chinh user ca nhan
    List<Lessonschedule> lessonschedules;

}
