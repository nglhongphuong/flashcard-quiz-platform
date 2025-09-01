package com.zotriverse.demo.dto.response.stats;
import com.zotriverse.demo.dto.response.UserInfoResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class StatsLessonResponse {
    private int lessonId;
    private int year;
    private Map<Integer, MonthQuizStats> months; // key: tháng
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MonthQuizStats {
        private int quizStudyCount;
        private List<QuizStudyInfo> quizStudies;

        private int testStudyCount;
        private List<TestStudyInfo> testStudies;
        // getters/setters
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class QuizStudyInfo {
        private Integer id;
        private Integer userId;
        private Date createdAt;
        UserInfoResponse userInfo;
        // getters/setters
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TestStudyInfo {
        private Integer quizId;
        private int min;
        private Date createdAt;
        UserInfoResponse userInfo;
        // getters/setters
    }
    // getters/setters
}
