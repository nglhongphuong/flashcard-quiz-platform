package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.request.QuizanswerRequest;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.QuizanswerResponse;
import com.zotriverse.demo.pojo.Quizhistory;
import com.zotriverse.demo.service.QuizanswerService;
import com.zotriverse.demo.service.QuizhistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin
public class QuizhistoryController {
    @Autowired
    private QuizhistoryService quizhistoryService;

    @Autowired
    private QuizanswerService quizanswerService;

    @DeleteMapping("/quiz-history/{quizhistoryId}")
    @PreAuthorize("@securityUtils.isQuizhistoryOwner(#quizhistoryId, authentication.name)")
    public ApiResponse<Void> deleteQuizhistory(@PathVariable int quizhistoryId) {
        quizhistoryService.deleteById(quizhistoryId); // chỉ gọi 1 lần
        return ApiResponse.<Void>builder()
                .message("Deleted successfully !! ")
                .build();
    }

    @PostMapping("/quiz-history/{quizhistoryId}/quiz-answer")
    @PreAuthorize("@securityUtils.isQuizhistoryOwner(#quizhistoryId, authentication.name)")
    public ApiResponse<QuizanswerResponse> createQuizAnswer(
            @PathVariable int quizhistoryId,
            @RequestBody QuizanswerRequest request) {
        request.setHistoryId(Quizhistory.builder().id(quizhistoryId).build()); // ép gán ID từ path
        return ApiResponse.<QuizanswerResponse>builder()
                .code(1000)
                .message("Tạo quiz answer thành công")
                .result(  quizanswerService.create(request))
                .build();
    }

    @PutMapping("/quiz-study/{quizStudyId}/reset")
    public ApiResponse<String> resetQuizHistory(@PathVariable Integer quizStudyId) {
        quizhistoryService.resetQuizHistoryByQuizStudyId(quizStudyId);
        return ApiResponse.<String>builder()
                .message("Quizhistory reset successfully")
                .build();
    }




}
