package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.request.QuizstudyRequest;
import com.zotriverse.demo.dto.request.TeststudyRequest;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.QuizstudyInfoResponse;
import com.zotriverse.demo.dto.response.QuizstudyResponse;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.service.AccountService;
import com.zotriverse.demo.service.QuizstudyService;
import com.zotriverse.demo.service.TeststudyService;
import com.zotriverse.demo.service.ViewhistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin
public class TeststudyController {
    @Autowired
    private TeststudyService teststudyService;
    @Autowired
    private QuizstudyService quizstudyService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ViewhistoryService viewhistoryService;

    @PostMapping("/lesson/{lessonId}/test-study")
    public ApiResponse<QuizstudyResponse> createTeststudy(
            @PathVariable int lessonId,
            @RequestBody TeststudyRequest request
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        QuizstudyResponse response = quizstudyService.createTeststudy(user.getId(), lessonId, request);
        viewhistoryService.viewLesson(user.getId(),lessonId);
        viewhistoryService.markAsStudied(user.getId(), lessonId);
        return ApiResponse.<QuizstudyResponse>builder()
                .code(1000)
                .message("Tạo test study thành công")
                .result(response)
                .build();
    }

    @GetMapping("/lesson/{lessonId}/test-study")
    public ApiResponse< List<QuizstudyInfoResponse> > getOnlyTestStudies(
            @PathVariable int lessonId,
            @RequestParam Map<String, String> params) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        params.put("userId", String.valueOf(user.getId()));
        params.put("lessonId", String.valueOf(lessonId));
        params.put("teststudy", "true");

        return ApiResponse.<List<QuizstudyInfoResponse>>builder()
                .code(1000)
                .result(quizstudyService.getQuizStudiesByLessonId(params))
                .build();
    }
}
