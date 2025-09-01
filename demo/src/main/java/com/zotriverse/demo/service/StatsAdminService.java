package com.zotriverse.demo.service;

import com.zotriverse.demo.dto.response.UserInfoResponse;
import com.zotriverse.demo.dto.response.stats.StatsAllResponse;
import com.zotriverse.demo.dto.response.stats.StatsLessonResponse;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Quizstudy;
import com.zotriverse.demo.pojo.Teststudy;
import com.zotriverse.demo.repository.StatsAdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsAdminService {

    @Autowired
    private StatsAdminRepository repo;

    public StatsAllResponse statsAll(int year) {
        StatsAllResponse response = new StatsAllResponse();
        response.setYear(year);
        Map<Integer, StatsAllResponse.MonthStats> monthMap = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            StatsAllResponse.MonthStats monthStats = new StatsAllResponse.MonthStats();

            List<Object[]> users = repo.findUsersByYearAndMonth(year, month);
            monthStats.setUserCount(users.size());
            List<StatsAllResponse.UserInfo> userInfos = users.stream().map(u -> {
                StatsAllResponse.UserInfo ui = new StatsAllResponse.UserInfo();
                ui.setId((Integer) u[0]);
                ui.setUsername((String) u[1]);
                ui.setName((String) u[2]);
                ui.setGender((String) u[3]);
                ui.setCreatedAt((Date) u[4]);
                return ui;
            }).collect(Collectors.toList());
            monthStats.setUsers(userInfos);

            List<Object[]> lessons = repo.findLessonsByYearAndMonth(year, month);
            monthStats.setLessonCount(lessons.size());
            List<StatsAllResponse.LessonInfo> lessonInfos = lessons.stream().map(l -> {
                StatsAllResponse.LessonInfo li = new StatsAllResponse.LessonInfo();
                li.setId((Integer) l[0]);
                li.setTitle((String) l[1]);
                li.setUserId((Integer) l[2]);
                li.setCreatedAt((Date) l[3]);
                return li;
            }).collect(Collectors.toList());
            monthStats.setLessons(lessonInfos);

            monthMap.put(month, monthStats);
        }
        response.setMonths(monthMap);
        return response;
    }

//    public StatsLessonResponse statsLesson(int lessonId, int year) {
//        StatsLessonResponse response = new StatsLessonResponse();
//        response.setLessonId(lessonId);
//        response.setYear(year);
//        Map<Integer, StatsLessonResponse.MonthQuizStats> monthMap = new HashMap<>();
//
//        for (int month = 1; month <= 12; month++) {
//            StatsLessonResponse.MonthQuizStats monthStats = new StatsLessonResponse.MonthQuizStats();
//
//            List<Quizstudy> quizStudies = repo.findQuizStudiesByLessonAndYearMonth(lessonId, year, month);
//            monthStats.setQuizStudyCount(quizStudies.size());
//            List<StatsLessonResponse.QuizStudyInfo> quizInfos = quizStudies.stream().map(q -> {
//                StatsLessonResponse.QuizStudyInfo qi = new StatsLessonResponse.QuizStudyInfo();
//                qi.setId(q.getId());
//                qi.setUserId(q.getUserId().getAccountId());
//                qi.setCreatedAt(q.getCreatedAt());
//                return qi;
//            }).collect(Collectors.toList());
//            monthStats.setQuizStudies(quizInfos);
//
//            List<Teststudy> testStudies = repo.findTestStudiesByLessonAndYearMonth(lessonId, year, month);
//            monthStats.setTestStudyCount(testStudies.size());
//            List<StatsLessonResponse.TestStudyInfo> testInfos = testStudies.stream().map(t -> {
//                StatsLessonResponse.TestStudyInfo ti = new StatsLessonResponse.TestStudyInfo();
//                ti.setQuizId(t.getQuizId());
//                ti.setMin(t.getMin());
//                ti.setCreatedAt(t.getQuizstudy().getCreatedAt());
//                return ti;
//            }).collect(Collectors.toList());
//            monthStats.setTestStudies(testInfos);
//
//            monthMap.put(month, monthStats);
//        }
//        response.setMonths(monthMap);
//        return response;
//    }


    public StatsLessonResponse statsLesson(int lessonId, int year) {
        StatsLessonResponse response = new StatsLessonResponse();
        response.setLessonId(lessonId);
        response.setYear(year);
        Map<Integer, StatsLessonResponse.MonthQuizStats> monthMap = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            StatsLessonResponse.MonthQuizStats monthStats = new StatsLessonResponse.MonthQuizStats();

            // ====== QuizStudies ======
            List<Quizstudy> quizStudies = repo.findQuizStudiesByLessonAndYearMonth(lessonId, year, month);
            monthStats.setQuizStudyCount(quizStudies.size());

            List<StatsLessonResponse.QuizStudyInfo> quizInfos = quizStudies.stream().map(q -> {
                StatsLessonResponse.QuizStudyInfo qi = new StatsLessonResponse.QuizStudyInfo();
                qi.setId(q.getId());
                qi.setUserId(q.getUserId().getAccountId());
                qi.setCreatedAt(q.getCreatedAt());

                Account acc = q.getUserId().getAccount();
                qi.setUserInfo(UserInfoResponse.builder()
                        .id(acc.getId())
                        .username(acc.getUsername())
                        .name(acc.getName())
                        .role(acc.getRole())
                        .avatar(acc.getAvatar())
                        .build());

                return qi;
            }).collect(Collectors.toList());
            monthStats.setQuizStudies(quizInfos);

            // ====== TestStudies ======
            List<Teststudy> testStudies = repo.findTestStudiesByLessonAndYearMonth(lessonId, year, month);
            monthStats.setTestStudyCount(testStudies.size());

            List<StatsLessonResponse.TestStudyInfo> testInfos = testStudies.stream().map(t -> {
                StatsLessonResponse.TestStudyInfo ti = new StatsLessonResponse.TestStudyInfo();
                ti.setQuizId(t.getQuizId());
                ti.setMin(t.getMin());
                ti.setCreatedAt(t.getQuizstudy().getCreatedAt());

                Account acc = t.getQuizstudy().getUserId().getAccount();
                ti.setUserInfo(UserInfoResponse.builder()
                        .id(acc.getId())
                        .username(acc.getUsername())
                        .name(acc.getName())
                        .role(acc.getRole())
                        .avatar(acc.getAvatar())
                        .build());

                return ti;
            }).collect(Collectors.toList());
            monthStats.setTestStudies(testInfos);

            monthMap.put(month, monthStats);
        }

        response.setMonths(monthMap);
        return response;
    }




}

