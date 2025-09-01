package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.request.QuizanswerRequest;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.QuizanswerResponse;
import com.zotriverse.demo.service.QuizanswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/quiz-answers")
@CrossOrigin
public class QuizanswerController {

    @Autowired
    private QuizanswerService quizanswerService;

    // CREATE


    @PreAuthorize("@securityUtils.isQuizanswerOwner(#id, authentication.name)")
    // UPDATE
    @PutMapping("/{id}")
    public ApiResponse<QuizanswerResponse> update(@PathVariable int id, @RequestBody QuizanswerRequest request) {
        return ApiResponse.<QuizanswerResponse>builder()
                .code(1000)
                .message("Cập nhật quiz answer thành công")
                .result(quizanswerService.update(id, request))
                .build();
    }

    // DELETE
    @PreAuthorize("@securityUtils.isQuizanswerOwner(#id, authentication.name)")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        quizanswerService.delete(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Xóa quiz answer thành công")
                .build();
    }
}

