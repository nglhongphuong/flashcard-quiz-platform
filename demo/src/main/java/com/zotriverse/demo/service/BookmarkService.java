package com.zotriverse.demo.service;

import com.zotriverse.demo.dto.request.BookmarkRequest;
import com.zotriverse.demo.dto.response.BookmarkResponse;
import com.zotriverse.demo.dto.response.LessonResponse;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.mapper.BookmarkMapper;
import com.zotriverse.demo.mapper.LessonMapper;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Bookmark;
import com.zotriverse.demo.pojo.BookmarkPK;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.repository.AccountRepository;
import com.zotriverse.demo.repository.BookmarkRepository;
import com.zotriverse.demo.repository.LessonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j //log cua lomboo inject 1 logger
public class BookmarkService {
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BookmarkMapper bookmarkMapper;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private LessonMapper lessonMapper;

    @Value("${PAGE_SIZE}")
    private int PAGE_SIZE;

    //Create  or Update
    public BookmarkResponse createOrDelete(int userId, int lessonId, BookmarkRequest request){
        Account account = accountRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        BookmarkPK pk = new BookmarkPK(userId,lessonId);
        Optional<Bookmark> bookmark = this.bookmarkRepository.findById(pk);
        if (bookmark.isPresent()) {
            bookmarkRepository.delete(bookmark.get());
            return null;
        }
        Bookmark save = Bookmark.builder()
                .bookmarkPK(pk)
                .user(account.getUser())
                .lesson(lesson)
                .build();
        Bookmark saved = bookmarkRepository.save(save);
        return bookmarkMapper.toBookmarkResponse(saved);
    }
    //lay danh sach cac lesson duoc danh dau (bookmark) cua userId,
    public List<LessonResponse> getBookmarkedLessons(int userId, Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
        Root<Bookmark> bookmarkRoot = query.from(Bookmark.class);
        Join<Bookmark, Lesson> lessonJoin = bookmarkRoot.join("lesson");// liên kết đến bài học
        query.select(lessonJoin);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(bookmarkRoot.get("bookmarkPK").get("userId")
                , userId));
        if (params.containsKey("lessonId")) {
            try {
                int lessonId = Integer.parseInt(params.get("lessonId"));
                predicates.add(builder.equal(lessonJoin.get("id"), lessonId));
            } catch (NumberFormatException e) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }
        }
        if (params.containsKey("startDate")) {
            LocalDateTime start = LocalDate.parse(params.get("startDate")).atStartOfDay();
            predicates.add(builder.greaterThanOrEqualTo(lessonJoin.get("createdAt"), start));
        }
        if (params.containsKey("endDate")) {
            LocalDateTime end = LocalDate.parse(params.get("endDate")).atTime(LocalTime.MAX);
            predicates.add(builder.lessThanOrEqualTo(lessonJoin.get("createdAt"), end));
        }
        String order = params.getOrDefault("order", "desc");
        if ("asc".equalsIgnoreCase(order)) {
            query.orderBy(builder.asc(lessonJoin.get("createdAt")));
        } else {
            query.orderBy(builder.desc(lessonJoin.get("createdAt")));
        }
        query.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Lesson> q = entityManager.createQuery(query);
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int size = PAGE_SIZE;
        q.setFirstResult((page - 1) * size);
        q.setMaxResults(size);
        return q.getResultList().stream().map(lessonMapper::toLessonResponse).toList();
    }

}
