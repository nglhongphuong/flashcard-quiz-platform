package com.zotriverse.demo.dto.response.stats;

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

public class StatsAllResponse {
    int year;
    Map<Integer, MonthStats> months; // key: tháng

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MonthStats {
        int userCount;
        List<UserInfo> users;
        int lessonCount;
        List<LessonInfo> lessons;
        // getters/setters
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)

    public static class UserInfo {
        Integer id;
        String username;
        String name;
        String gender;
        Date createdAt;
        // getters/setters
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class LessonInfo {
        Integer id;
        String title;
        Integer userId;
        Date createdAt;
        // getters/setters
    }
    // getters/setters
}

