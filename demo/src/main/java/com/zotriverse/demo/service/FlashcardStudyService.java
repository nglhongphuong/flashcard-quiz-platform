package com.zotriverse.demo.service;

import com.zotriverse.demo.dto.response.FlashcardResponse;
import com.zotriverse.demo.dto.response.FlashcardStudyResponse;
import com.zotriverse.demo.dto.response.FlashcardStudyStatsResponse;
import com.zotriverse.demo.dto.response.FlashcardStudyStatusResponse;
import com.zotriverse.demo.enums.Status;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.mapper.FlashcardStudyMapper;
import com.zotriverse.demo.pojo.*;
import com.zotriverse.demo.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class FlashcardStudyService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FlashcardStudyRepository flashcardStudyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FlashCardRepository flashcardRepository;

    @Autowired
    private FlashcardStudyMapper flashcardStudyMapper;

    @Autowired
    private LessonRepository lessonRepository;


//

    public List<FlashcardStudyResponse> createFlashcardStudyByLessonIfNotExists(int userId,int lessonId) {
        // Lấy danh sách flashcard thuộc lesson
        List<Flashcard> flashcards = flashcardRepository.findByLessonId(lessonId);
        List<Integer> flashcardIds = flashcards.stream().map(Flashcard::getId).toList();

        // Lấy danh sách flashcardStudy đã tồn tại của user
        List<FlashcardStudy> existingStudies = flashcardStudyRepository.findByUserIdAndFlashcardIds(userId, flashcardIds);
        Set<Integer> existingFlashcardIds = existingStudies.stream()
                .map(fs -> fs.getFlashcard().getId())
                .collect(Collectors.toSet());

        // Lọc ra flashcard mới mà user chưa học
        List<Flashcard> newFlashcards = flashcards.stream()
                .filter(f -> !existingFlashcardIds.contains(f.getId()))
                .toList();

        if (newFlashcards.isEmpty()) {
            log.info("User {} already has flashcardStudy for all flashcards in lesson {}", userId, lessonId);
            return Collections.emptyList();
        }

        // Tạo flashcardStudy cho các flashcard mới
        List<FlashcardStudy> studyList = newFlashcards.stream()
                .map(flashcard -> FlashcardStudy.builder()
                        .flashcardStudyPK(new FlashcardStudyPK(userId, flashcard.getId()))
                        .status(Status.NOT_LEARNED.getDisplayName())
                        .flashcard(flashcard)
                        .user(userRepository.getReferenceById(userId))
                        .build())
                .toList();

        List<FlashcardStudy> results = flashcardStudyRepository.saveAll(studyList);
        return results.stream().map(flashcardStudyMapper::toFlashcardStudyResponse).toList();
    }


    @Transactional
    public List<FlashcardStudyResponse> deleteFlashcardStudyByLessonId(int lessonId) {
        // Lấy username từ SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Lấy User từ username
        Account user = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        int userId = user.getId();

        // Kiểm tra lesson tồn tại
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

        // Lấy danh sách flashcardId thuộc lesson
        List<Flashcard> flashcards = flashcardRepository.findByLessonId(lessonId);
        List<Integer> flashcardIds = flashcards.stream().map(Flashcard::getId).toList();

        if (flashcardIds.isEmpty()) {
            throw new AppException(ErrorCode.CANNOT_FOUND_FLASHCARD_STUDY);
        }

        // Lấy danh sách FlashcardStudy để trả về
        List<FlashcardStudy> studies = flashcardStudyRepository
                .findByUserIdAndFlashcardIds(userId, flashcardIds);

        // Nếu không có gì để xóa thì thôi
        if (studies.isEmpty()) {
            throw new AppException(ErrorCode.CANNOT_FOUND_FLASHCARD_STUDY);
        }

        // Xóa
        flashcardStudyRepository.deleteAll(studies);
        log.info("Đã xóa {} flashcardStudy của user {} cho lesson {}", studies.size(), userId, lessonId);

        // Trả về danh sách đã xóa
        return studies.stream()
                .map(flashcardStudyMapper::toFlashcardStudyResponse)
                .toList();
    }

    @Transactional
    public List<FlashcardStudyResponse> resetFlashcardStudyStatus(int lessonId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        int userId = user.getId();
        // Lấy danh sách flashcardId từ lesson
        List<Flashcard> flashcards = flashcardRepository.findByLessonId(lessonId);
        if (flashcards.isEmpty()) {
            throw new AppException(ErrorCode.CANNOT_FOUND);
        }
        List<Integer> flashcardIds = flashcards.stream().map(Flashcard::getId).toList();
        // Tìm danh sách flashcardStudy cần cập nhật
        List<FlashcardStudy> studies = flashcardStudyRepository.findByUserIdAndFlashcardIds(userId, flashcardIds);

        if (studies.isEmpty()) {
            throw new AppException(ErrorCode.CANNOT_FOUND);
        }
        // Cập nhật tất cả về trạng thái "NOT_LEARNED"
        for (FlashcardStudy study : studies) {
            study.setStatus(Status.NOT_LEARNED.getDisplayName());
            study.setUpdateAt(new Date());
        }
        List<FlashcardStudy> updated = flashcardStudyRepository.saveAll(studies);
        return updated.stream().map(flashcardStudyMapper::toFlashcardStudyResponse).toList();
    }
    //Bo sung nguoi dung la nguoi thuoc flashcard hoc do
    @Transactional
    public FlashcardStudyResponse updateStatus(int flashcardId, String status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        int userId = user.getId();
        FlashcardStudy study = flashcardStudyRepository.findByUserIdAndFlashcardId(userId, flashcardId)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
        Status s;
        try {
            s = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        study.setStatus(s.getDisplayName());
        flashcardStudyRepository.save(study);
        return flashcardStudyMapper.toFlashcardStudyResponse(study);
    }

//    @Transactional(readOnly = true)
//    public FlashcardStudyStatusResponse getFlashcardStudy(int lessonId, int userId, String status) {
//
//        // B1: kiểm tra đã học chưa
//        boolean learned = flashcardStudyRepository.existsByUserIdAndLessonId(userId, lessonId);
//        if (!learned) {
//            throw new AppException(ErrorCode.UNAUTHORIZED);
//        }
//
//        // B2: Lấy danh sách flashcard thuộc lesson
//        List<Flashcard> flashcardsInLesson = flashcardRepository.findByLessonId(lessonId);
//        List<Integer> flashcardIds = flashcardsInLesson.stream()
//                .map(Flashcard::getId)
//                .toList();
//
//        // B3: Truy vấn FlashcardStudy theo userId + status + flashcardIds
//        List<FlashcardStudy> flashcardStudy = flashcardStudyRepository
//                .findByUserIdAndStatusAndFlashcardIds(userId, status, flashcardIds);
//
//      //B4: Từ flashcardStudy vừa lấy được - truy ngược lại flashcard
//        List<FlashcardResponse> flashcards = flashcardStudy.stream()
//                .map(fs -> {
//                    Flashcard f = fs.getFlashcard();
//                    return new FlashcardResponse(
//                            f.getId(),
//                            f.getWord(),
//                            f.getDefinition(),
//                            f.getImage(),
//                            f.getCreatedAt(),
//                            f.getUpdateAt(),
//                            f.getLessonId()
//                    );
//                })
//                .toList();
//        return FlashcardStudyStatusResponse.builder()
//                .status(status)
//                .num(flashcards.size())
//                .flashcardSet(flashcards)
//                .build();
//    }
//

    @Transactional(readOnly = true)
    public FlashcardStudyStatusResponse getFlashcardStudyFlexible(int lessonId, int userId, String status) {
        // B1: kiểm tra đã học chưa
        boolean learned = flashcardStudyRepository.existsByUserIdAndLessonId(userId, lessonId);
        if (!learned) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // B2: Lấy tất cả flashcard thuộc lesson
        List<Flashcard> flashcardsInLesson = flashcardRepository.findByLessonId(lessonId);
        List<Integer> flashcardIds = flashcardsInLesson.stream()
                .map(Flashcard::getId)
                .toList();

        // B3: Lấy flashcardStudy theo userId + flashcardIds, có thể lọc theo status nếu truyền
        List<FlashcardStudy> flashcardStudyList;
        String resultStatus;

        if (status != null && !status.isBlank()) {
            flashcardStudyList = flashcardStudyRepository
                    .findByUserIdAndStatusAndFlashcardIds(userId, status, flashcardIds);
            resultStatus = status;
        } else {
            flashcardStudyList = flashcardStudyRepository
                    .findByUserIdAndFlashcardIds(userId, flashcardIds);
            resultStatus = "ALL";
        }

        // B4: Convert sang FlashcardResponse
        List<FlashcardResponse> flashcards = flashcardStudyList.stream()
                .map(fs -> {
                    Flashcard f = fs.getFlashcard();
                    return new FlashcardResponse(
                            f.getId(),
                            f.getWord(),
                            f.getDefinition(),
                            f.getImage(),
                            f.getCreatedAt(),
                            f.getUpdateAt(),
                            f.getLessonId()
                    );
                })
                .toList();

        return FlashcardStudyStatusResponse.builder()
                .status(resultStatus)
                .num(flashcards.size())
                .flashcardSet(flashcards)
                .build();
    }


    @Transactional(readOnly = true)
    public FlashcardStudyStatsResponse getFlashcardStudyStats(int lessonId, int userId) {
        // Gọi lại hàm cũ để lấy số lượng từng trạng thái
        FlashcardStudyStatusResponse all = getFlashcardStudyFlexible(lessonId, userId, null);
        FlashcardStudyStatusResponse remembered = getFlashcardStudyFlexible(lessonId, userId, Status.REMEMBERED.getDisplayName());
        FlashcardStudyStatusResponse notLearned = getFlashcardStudyFlexible(lessonId, userId, Status.NOT_LEARNED.getDisplayName());
        FlashcardStudyStatusResponse notRemembered = getFlashcardStudyFlexible(lessonId, userId, Status.NOT_REMEMBERED.getDisplayName());

        return FlashcardStudyStatsResponse.builder()
                .lessonId(lessonId)
                .ALL(all.getNum())
                .REMEMBERED(remembered.getNum())
                .NOT_LEARNED(notLearned.getNum())
                .NOT_REMEMBERED(notRemembered.getNum())
                .build();
    }








}
