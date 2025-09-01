package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.FlashcardStudyResponse;
import com.zotriverse.demo.dto.response.FlashcardStudyStatsResponse;
import com.zotriverse.demo.dto.response.FlashcardStudyStatusResponse;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.service.AccountService;
import com.zotriverse.demo.service.FlashcardStudyService;
import com.zotriverse.demo.service.ViewhistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j //log cua lomboo inject 1 logger
@CrossOrigin
public class FlashcardStudyController {
    @Autowired
    private FlashcardStudyService flashcardStudyService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ViewhistoryService viewhistoryService;

    //================ FLASHCARD STUDY =================================================================================
    @PostMapping("/lesson/{lessonId}/flashcard-study")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<FlashcardStudyResponse>> startFlashcardStudy(
            @PathVariable int lessonId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        viewhistoryService.viewLesson(user.getId(),lessonId);
        viewhistoryService.markAsStudied(user.getId(), lessonId);

        List<FlashcardStudyResponse> responses = flashcardStudyService.createFlashcardStudyByLessonIfNotExists(user.getId(),lessonId);
        return ApiResponse.<List<FlashcardStudyResponse>>builder()
                .result(responses)
                .message("Flashcard study initialized.")
                .build();
    }

    @DeleteMapping("/lesson/{lessonId}/flashcard-study")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<FlashcardStudyResponse>> deleteFlashcardStudyByLessonId(
            @PathVariable int lessonId
    ) {
        List<FlashcardStudyResponse> deletedStudies = flashcardStudyService.deleteFlashcardStudyByLessonId(lessonId);
        return ApiResponse.<List<FlashcardStudyResponse>>builder()
                .result(deletedStudies)
                .message("Flashcard study deleted successfully.")
                .build();
    }

    @PutMapping("/lesson/{lessonId}/flashcard-study/reset")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<FlashcardStudyResponse>> resetFlashcardStudy(
            @PathVariable("lessonId") int lessonId
    ) {
        List<FlashcardStudyResponse> responses = flashcardStudyService.resetFlashcardStudyStatus(lessonId);
        return ApiResponse.<List<FlashcardStudyResponse>>builder()
                .result(responses)
                .message("Flashcard study status reset to NOT_LEARNED.")
                .build();
    }

    //==================== Flash card study ==================================
    //Cập nhập trạng thái đã học, nhớ, hoặc bỏ qua.
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/flashcard/{flashcardId}/flashcard-study")
    public ApiResponse<FlashcardStudyResponse> updateFlashcardStudyStatus(
            @PathVariable int flashcardId,
            @RequestParam("status") String status) {
        return ApiResponse.<FlashcardStudyResponse>builder()
                .result(flashcardStudyService.updateStatus(flashcardId, status))
                .message("Updated Successfully !")
                .build();
    }

//    @GetMapping("/flashcard-study")
//    public ApiResponse<FlashcardStudyStatusResponse> testGetFlashcardStudy(
//            @RequestParam int lessonId,
//            @RequestParam int userId,
//            @RequestParam String status
//    ) {
//        return ApiResponse.<FlashcardStudyStatusResponse>builder()
//                .result(flashcardStudyService.getFlashcardStudy(lessonId, userId, status))
//                .build();
//    }

    // /lesson/{lessonId}/flashcard-study/status
    @GetMapping("/lesson/{lessonId}/flashcard-study/status")
    public ApiResponse<FlashcardStudyStatusResponse> getFlashcardsByLessonAndStatus(
            @PathVariable int lessonId,
            @RequestParam(required = false) String status
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        return ApiResponse.<FlashcardStudyStatusResponse>builder()
                .result(flashcardStudyService.getFlashcardStudyFlexible(lessonId, user.getId(), status))
                .build();

    }

    @GetMapping("/lesson/{lessonId}/flashcard-study/stats")
    public  ApiResponse<FlashcardStudyStatsResponse> getFlashcardStats(@PathVariable int lessonId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        return ApiResponse.<FlashcardStudyStatsResponse>builder()
                .result(flashcardStudyService.getFlashcardStudyStats(lessonId,user.getId()))
                .build();
    }








}
