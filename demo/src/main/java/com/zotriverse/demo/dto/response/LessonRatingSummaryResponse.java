package com.zotriverse.demo.dto.response;

import com.zotriverse.demo.enums.Star;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor // constructor ko tham so
@AllArgsConstructor // constructor cos tham soo
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonRatingSummaryResponse {
    private Integer lessonId;
    private String title;
    private String description;
    private String image;
    private Float averageRating; // trung binh danh  gia
    //bo sung so luong nguoi hoc trong cung 1 lesson
    private Long totalUser;
    private Map<Star, Long> ratingDistribution;
}
