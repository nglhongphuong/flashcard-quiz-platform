package com.zotriverse.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.LessonResponse;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.mapper.AccountMapper;
import com.zotriverse.demo.mapper.LessonMapper;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.User;
import com.zotriverse.demo.repository.AccountRepository;
import com.zotriverse.demo.repository.LessonRepository;
import com.zotriverse.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private Cloudinary cloudinary;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${PAGE_SIZE}")
    private int PAGE_SIZE;

    //Get - all - by admin
    public List<LessonResponse> getLessons(Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
        Root<Lesson> root = query.from(Lesson.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey("id")) {
            try {
                int lessonId = Integer.parseInt(params.get("id"));
                predicates.add(builder.equal(root.get("id"), lessonId));
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find Id Lesson.");
            }
        }
        String keyword = params.get("search");
        if (keyword != null && !keyword.isBlank()) {
            Predicate titleLike = builder.like(root.get("title"), "%" + keyword + "%");
            Predicate descLike = builder.like(root.get("description"), "%" + keyword + "%");
            predicates.add(builder.or(titleLike, descLike));
        }




        if (params.containsKey("userId")) {
            try {
                int userId = Integer.parseInt(params.get("userId"));
                predicates.add(builder.equal(root.get("userId").get("accountId"), userId));
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId không hợp lệ.");
            }
        }

        // Lọc theo username người tạo (JOIN với Account)
        if (params.containsKey("username")) {
            String username = params.get("username");
            if (username != null && !username.isBlank()) {
                Join<Lesson, User> userJoin = root.join("userId");
                Join<User, Account> accountJoin = userJoin.join("account");
                predicates.add(builder.like(accountJoin.get("username"), "%" + username + "%"));
            }
        }

        // Lọc theo khoảng thời gian createdAt
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");

        if (startDate != null && !startDate.isBlank()) {
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), start));
        }

        if (endDate != null && !endDate.isBlank()) {
            LocalDateTime end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
            predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), end));
        }

        // Sắp xếp
        String order = params.getOrDefault("order", "desc");
        if ("asc".equalsIgnoreCase(order)) {
            query.orderBy(builder.asc(root.get("createdAt")));
        } else {
            query.orderBy(builder.desc(root.get("createdAt")));
        }

        // Thêm điều kiện where
        query.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Lesson> q = entityManager.createQuery(query);

        // Phân trang
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int size = PAGE_SIZE;

        q.setFirstResult((page - 1) * size);
        q.setMaxResults(size);

        List<Lesson> results = q.getResultList();
        return results.stream().map(lessonMapper::toLessonResponse).toList();
    }
    //create - owner user
    public LessonResponse createLesson(Map<String, String> params, MultipartFile avatar){
        //lay user tao lesson
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account user = accountRepo.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        log.info("Account from DB: {}", user.getRole());
        Lesson lesson = new Lesson();
        lesson.setUserId(user.getUser());
        lesson.setTitle(params.get("title"));
        lesson.setDescription(params.get("description"));
        lesson.setVisibility(params.get("visibility"));
        boolean isLocked = false;
        if (params.containsKey("isCommentLocked")) {
            String value = params.get("isCommentLocked");
            if (value != null && !value.isBlank()) {
                isLocked = Boolean.parseBoolean(value);
            }
        }
        lesson.setIsCommentLocked(isLocked);
        //xu lý image = dat ten avatar cho le =))  nếu có
        if (avatar != null && !avatar.isEmpty()) {
            try {
                File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + avatar.getOriginalFilename());
                try (FileOutputStream fos = new FileOutputStream(convFile)) {
                    fos.write(avatar.getBytes());
                }
                var pic = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/avatars/"));
                lesson.setImage(pic.get("url").toString());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Không thể tải ảnh lên.");
            }
        }
        this.lessonRepository.save(lesson);
        //Lay id lesson vua moi tao . howwww ???
        log.info("Tạo bài học thành công, ID: {}", lesson.getId());
        return lessonMapper.toLessonResponse(lesson);
    }

    public LessonResponse createLesson(int accountId,Map<String, String> params, MultipartFile avatar){
        Account user = accountRepo.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        log.info("Account from DB: {}", user.getRole());
        Lesson lesson = new Lesson();
        lesson.setUserId(user.getUser());
        lesson.setTitle(params.get("title"));
        lesson.setDescription(params.get("description"));
        lesson.setVisibility(params.get("visibility"));
        boolean isLocked = false;
        if (params.containsKey("isCommentLocked")) {
            String value = params.get("isCommentLocked");
            if (value != null && !value.isBlank()) {
                isLocked = Boolean.parseBoolean(value);
            }
        }
        lesson.setIsCommentLocked(isLocked);
        // Xử lý image = dat ten avatar cho le =))  nếu có
        if (avatar != null && !avatar.isEmpty()) {
            try {
                File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + avatar.getOriginalFilename());
                try (FileOutputStream fos = new FileOutputStream(convFile)) {
                    fos.write(avatar.getBytes());
                }

                var pic = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/avatars/"));
                lesson.setImage(pic.get("url").toString());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Không thể tải ảnh lên.");
            }
        }
        this.lessonRepository.save(lesson);
        //Lay id lesson vua moi tao . howwww ???
        log.info("Tạo bài học thành công, ID: {}", lesson.getId());
        return lessonMapper.toLessonResponse(lesson);
    }

    //update - id lesson - by owner user, user khac bi denined
    private LessonResponse updateLesson(Lesson lesson, Map<String, String> params, MultipartFile avatar) {
        if (params.containsKey("title")) {
            lesson.setTitle(params.get("title"));
        }
        if (params.containsKey("description")) {
            lesson.setDescription(params.get("description"));
        }
        if (params.containsKey("visibility")) {
            lesson.setVisibility(params.get("visibility"));
        }

        if (params.containsKey("isCommentLocked")) {
            String value = params.get("isCommentLocked");
            if (value != null && !value.isBlank()) {
                lesson.setIsCommentLocked(Boolean.parseBoolean(value));
            }
        }
        // Cập nhật ảnh nếu có
        if (avatar != null && !avatar.isEmpty()) {
            try {
                File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + avatar.getOriginalFilename());
                try (FileOutputStream fos = new FileOutputStream(convFile)) {
                    fos.write(avatar.getBytes());
                }

                var pic = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/avatars/"));
                lesson.setImage(pic.get("url").toString());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Không thể tải ảnh lên.");
            }
        }
        lessonRepository.save(lesson);
        return lessonMapper.toLessonResponse(lesson);
    }

    public LessonResponse updateLesson(int lessonId, Map<String, String> params, MultipartFile avatar) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepo.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean isAuthor = isOwner(lessonId, username);
        boolean isAdmin = "ADMIN".equalsIgnoreCase(account.getRole());

        if (!isAuthor && !isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return updateLesson(lesson, params, avatar);
    }
    public boolean isOwner(int lessonId, String username) {
        return lessonRepository.findById(lessonId)
                .map(lesson -> {
                    if (lesson.getUserId() == null) return false;
                    Account account = lesson.getUserId().getAccount(); // giả sử lesson.userId → User → Account
                    return account != null && username.equals(account.getUsername());
                })
                .orElse(false); // nếu không tìm thấy bài học → không phải owner
    }



    //delete - id - by owner user, user khac bi denied
    public void deleteLesson(int lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepo.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean isAuthor = isOwner(lessonId, username);
        boolean isAdmin = "ADMIN".equalsIgnoreCase(account.getRole());

        if (!isAuthor && !isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        lessonRepository.delete(lesson);
        log.info("Đã xóa bài học id = {}, bởi user = {}", lessonId, username);
    }

    public LessonResponse getLessonById(int lessonId){
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        return lessonMapper.toLessonResponse(lesson);
    }


}
