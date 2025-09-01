package com.zotriverse.demo.service;

import com.zotriverse.demo.dto.request.RatingRequest;
import com.zotriverse.demo.dto.response.LessonRatingSummaryResponse;
import com.zotriverse.demo.dto.response.RatingResponse;
import com.zotriverse.demo.enums.Star;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.mapper.RatingMapper;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.Rating;
import com.zotriverse.demo.pojo.RatingPK;
import com.zotriverse.demo.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j //log cua lomboo inject 1 logger
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private RatingMapper ratingMapper;
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private FlashcardStudyRepository flashcardStudyRepository;
    @Autowired
    private QuizstudyRepository quizStudyRepository;

    @Autowired
    private AccountRepository accountRepository;
    public RatingResponse createOrUpdateRating(int userId, int lessonId, RatingRequest rate) {
        RatingPK ratingPK = new RatingPK(userId, lessonId);
        Rating rating = ratingRepository.findById(ratingPK).orElse(null);
        Account account = accountRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        lessonRepository.findById(lessonId).orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        if (rating == null) {
            // Nếu chưa đánh giá → tạo mới
            rating = Rating.builder()
                    .ratingPK(ratingPK)
                    .user(account.getUser())
                    .lesson(lessonRepository.findById(lessonId).orElseThrow(() -> new RuntimeException("Lesson not found")))
                    .star(rate.getStar())
                    .build();
        } else {
            rating.setStar(rate.getStar());
        }
        Rating saved = ratingRepository.save(rating);
        return ratingMapper.toRatingResponse(saved);
    }
    public void deleteRating(int userId, int lessonId) {
        RatingPK ratingPK = new RatingPK(userId, lessonId);
        Rating rating = ratingRepository.findById(ratingPK).orElse(null);
        if (rating == null) {
            throw new AppException(ErrorCode.CANNOT_FOUND);
        }
        ratingRepository.delete(rating);
    }
    public LessonRatingSummaryResponse getLessonRatingSummary(int lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        List<Object[]> rawDistribution = ratingRepository.countRatingDistribution(lessonId);
        Map<Star, Long> ratingMap = new EnumMap<>(Star.class);
        for (Star s : Star.values()) {
            ratingMap.put(s, 0L);
        }
        for (Object[] row : rawDistribution) {
            Character starChar = (Character) row[0];
            Star star = Star.fromChar(starChar);
            Long count = (Long) row[1];
            ratingMap.put(star, count);
        }
        Float average = ratingRepository.findAverageRating(lessonId);

        Set<Integer> userIds = new HashSet<>();

        userIds.addAll(flashcardStudyRepository.findUserIdsByLessonId(lessonId));
        userIds.addAll(quizStudyRepository.findUserIdsByLessonId(lessonId));
        long totalUniqueLearners = userIds.size();

        return LessonRatingSummaryResponse.builder()
                .lessonId(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .image(lesson.getImage())
                .averageRating(average != null ? Math.round(average * 10) / 10.0f : null)
                .ratingDistribution(ratingMap)
                .totalUser(totalUniqueLearners)
                .build();
    }




}
