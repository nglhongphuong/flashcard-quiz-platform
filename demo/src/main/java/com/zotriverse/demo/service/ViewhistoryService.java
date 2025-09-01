package com.zotriverse.demo.service;

import com.zotriverse.demo.dto.response.LessonResponse;
import com.zotriverse.demo.dto.response.UserInfoResponse;
import com.zotriverse.demo.dto.response.ViewhistoryResponse;
import com.zotriverse.demo.dto.response.LessonViewHistoryResponse;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.mapper.AccountMapper;
import com.zotriverse.demo.mapper.LessonMapper;
import com.zotriverse.demo.mapper.ViewhistoryMapper;
import com.zotriverse.demo.pojo.*;
import com.zotriverse.demo.repository.AccountRepository;
import com.zotriverse.demo.repository.LessonRepository;
import com.zotriverse.demo.repository.LessonscheduleRepository;
import com.zotriverse.demo.repository.ViewhistoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j //log cua lomboo inject 1 logger
public class ViewhistoryService {
    @Autowired
    private ViewhistoryRepository viewhistoryRepository;

    @Autowired
    private ViewhistoryMapper viewhistoryMapper;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LessonscheduleRepository lessonscheduleRepository;



    @Value("${PAGE_SIZE}")
    private int PAGE_SIZE;

    @Value("${VIEW_HISTORY_EXPIRED_DAY}")
    private int VIEW_HISTORY_EXPIRED_DAY;

    //Tao 1 ham view history de goi khi ma nguoi dung nhan vao lesson de xem =)) xay ra khi
    //1. Nhan vao xem chi tiet bai hoc
    // Neu chi xem be ngoai,( chua hoc flashcard/ study Quiz, Test Study ) -> Not learn -> thiet lap thoi gian neu qua 5 ngay tu dong xoa khoi lich su xem
    // Neu co tac dong hoc (vd nhu hoc flashcard (100% -> luu lich su hoc tru khi nguoi dung xoa lich su, la xoa du lieu co trong flashcard, quiz study, test quiz nguoi dung) -> xoa view history
    // Update khi xay ra qua trinh hoc
    //Chuc nang lich su lam bai, update theo qua trinh vao flashcard =))) goi ham khi vao xem lesson .
    public void viewLesson(int userId, int lessonId) {
        ViewhistoryPK pk = new ViewhistoryPK(userId, lessonId);
        Optional<Viewhistory> optional = viewhistoryRepository.findById(pk);

        if (optional.isPresent()) {
            Viewhistory history = optional.get();
            history.setUpdateAt(new Date());
            viewhistoryRepository.save(history);
        } else {
            Account account = accountRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
            Viewhistory history = Viewhistory.builder()
                    .viewhistoryPK(pk)
                    .user(account.getUser())
                    .lesson(lesson)
                    .study(false)
                    .build();
            viewhistoryRepository.save(history);
        }
    }
    public void markAsStudied(int userId, int lessonId) {
        ViewhistoryPK pk = new ViewhistoryPK(userId, lessonId);
        Viewhistory history = viewhistoryRepository.findById(pk)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        history.setStudy(true);
        history.setUpdateAt(new Date());
        viewhistoryRepository.save(history);
    }
    //Them DTO View history chua them thong tin thoi gian va study khi user truy van

    public List<LessonResponse> getLessonsByViewHistory(int userId,Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);

        Root<Viewhistory> vhRoot = query.from(Viewhistory.class);
        Join<Viewhistory, Lesson> lessonJoin = vhRoot.join("lesson");
        query.select(lessonJoin);
        List<Predicate> predicates = new ArrayList<>();
        Root<Lesson> root = query.from(Lesson.class);
        accountRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        predicates.add(builder.equal(vhRoot.get("viewhistoryPK").get("userId"), userId));

        if (params.containsKey("study")) {
            String studyStr = params.get("study");
            if ("true".equalsIgnoreCase(studyStr)) {
                predicates.add(builder.equal(vhRoot.get("study"), true));
            } else if ("false".equalsIgnoreCase(studyStr)) {
                predicates.add(builder.equal(vhRoot.get("study"), false));
            } else {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }
        }

        String keyword = params.get("search");
        if (keyword != null && !keyword.isBlank()) {
            Predicate titleLike = builder.like(root.get("title"), "%" + keyword + "%");
            Predicate descLike = builder.like(root.get("description"), "%" + keyword + "%");
            predicates.add(builder.or(titleLike, descLike));
        }

        if (params.containsKey("lessonId")) {
            try {
                int lessonId = Integer.parseInt(params.get("lessonId"));
                predicates.add(builder.equal(lessonJoin.get("id"), lessonId));
            } catch (NumberFormatException e) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }
        }
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        if (startDate != null && !startDate.isBlank()) {
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            predicates.add(builder.greaterThanOrEqualTo(vhRoot.get("updateAt"), start));
        }
        if (endDate != null && !endDate.isBlank()) {
            LocalDateTime end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
            predicates.add(builder.lessThanOrEqualTo(vhRoot.get("updateAt"), end));
        }
        String order = params.getOrDefault("order", "desc");
        if ("asc".equalsIgnoreCase(order)) {
            query.orderBy(builder.asc(vhRoot.get("updateAt")));
        } else {
            query.orderBy(builder.desc(vhRoot.get("updateAt")));
        }
        query.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Lesson> q = entityManager.createQuery(query);
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int size = PAGE_SIZE;
        q.setFirstResult((page - 1) * size);
        q.setMaxResults(size);
        return q.getResultList().stream()
                .map(lessonMapper::toLessonResponse)
                .toList();
    }



    public List<LessonViewHistoryResponse> getLessonViewHistoryResponse(int userId, Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Viewhistory> query = builder.createQuery(Viewhistory.class);
        Root<Viewhistory> vhRoot = query.from(Viewhistory.class);
        Join<Viewhistory, Lesson> lessonJoin = vhRoot.join("lesson");
        List<Predicate> predicates = new ArrayList<>();
        accountRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        predicates.add(builder.equal(vhRoot.get("viewhistoryPK").get("userId"), userId));
        if (params.containsKey("study")) {
            String studyStr = params.get("study");
            if ("true".equalsIgnoreCase(studyStr)) {
                predicates.add(builder.equal(vhRoot.get("study"), true));
            } else if ("false".equalsIgnoreCase(studyStr)) {
                predicates.add(builder.equal(vhRoot.get("study"), false));
            } else {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }
        }
        if (params.containsKey("lessonId")) {
            try {
                int lessonId = Integer.parseInt(params.get("lessonId"));
                predicates.add(builder.equal(lessonJoin.get("id"), lessonId));
            } catch (NumberFormatException e) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }
        }
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        if (startDate != null && !startDate.isBlank()) {
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            predicates.add(builder.greaterThanOrEqualTo(vhRoot.get("updateAt"), start));
        }
        if (endDate != null && !endDate.isBlank()) {
            LocalDateTime end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
            predicates.add(builder.lessThanOrEqualTo(vhRoot.get("updateAt"), end));
        }
        String order = params.getOrDefault("order", "desc");
        if ("asc".equalsIgnoreCase(order)) {
            query.orderBy(builder.asc(vhRoot.get("updateAt")));
        } else {
            query.orderBy(builder.desc(vhRoot.get("updateAt")));
        }
        query.select(vhRoot).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Viewhistory> q = entityManager.createQuery(query);
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int size = PAGE_SIZE;
        q.setFirstResult((page - 1) * size);
        q.setMaxResults(size);

        List<Viewhistory> results = q.getResultList();

        return results.stream().map(viewhistory -> {
            Lesson lesson = viewhistory.getLesson();
            ViewhistoryResponse vhResponse = viewhistoryMapper.toViewhistoryResponse(viewhistory);
            UserInfoResponse userInfo = accountMapper.toUserInfo(lesson.getUserId().getAccount());

            List<Lessonschedule> schedules = lessonscheduleRepository
                    .findAllByUserIdAndLessonId(userId, lesson.getId());

            return LessonViewHistoryResponse.builder()
                    .id(lesson.getId())
                    .title(lesson.getTitle())
                    .description(lesson.getDescription())
                    .image(lesson.getImage())
                    .visibility(lesson.getVisibility())
                    .isCommentLocked(lesson.getIsCommentLocked())
                    .createdAt(lesson.getCreatedAt())
                    .updateAt(lesson.getUpdateAt())
                    .flashcardSet(lesson.getFlashcardSet())
                    .userInfo(userInfo)
                    .viewhistoryResponse(vhResponse)
                    .lessonschedules(schedules)
                    .build();
        }).toList();

    }


    //Ngày upate view history - study: False nhỏ hơn ngày hiện tại quá 7 nngà    (DEFAULT DELETE IN VENV PROPERTIES)  -> hệ thống tự xóa viewhistory
    @Transactional
    //Mỗi ngày lúc 3 giờ sáng
    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteExpiredViewHistory() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(VIEW_HISTORY_EXPIRED_DAY);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Viewhistory> delete = builder.createCriteriaDelete(Viewhistory.class);
        Root<Viewhistory> root = delete.from(Viewhistory.class);

        Predicate condition = builder.and(
                builder.equal(root.get("study"), false),
                builder.lessThan(root.get("updateAt"), Timestamp.valueOf(threshold))
        );
        delete.where(condition);
        int deletedCount = entityManager.createQuery(delete).executeUpdate();
        log.info("Auto-deleted {} expired view history records.", deletedCount);
    }
}
