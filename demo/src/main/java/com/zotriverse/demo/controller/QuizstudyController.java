package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.request.QuizHistoryBulkUpdateRequest;
import com.zotriverse.demo.dto.request.QuizHistoryUpdateRequest;
import com.zotriverse.demo.dto.request.QuizstudyRequest;
import com.zotriverse.demo.dto.response.*;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.service.AccountService;
import com.zotriverse.demo.service.FlashcardStudyService;
import com.zotriverse.demo.service.QuizstudyService;
import com.zotriverse.demo.service.ViewhistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin
public class QuizstudyController {
    @Autowired
    private QuizstudyService quizstudyService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private ViewhistoryService viewhistoryService;

    @Autowired
    private FlashcardStudyService flashcardStudyService;

    //Tao
    @PostMapping("/lesson/{lessonId}/quiz-study")
    public ApiResponse<QuizstudyResponse> createQuizStudy(@PathVariable int lessonId,
                                                          @RequestBody QuizstudyRequest quizstudyRequest){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        viewhistoryService.viewLesson(user.getId(),lessonId);
        viewhistoryService.markAsStudied(user.getId(), lessonId);
//Gọi api tạo flashcard trước nếu ko có á fen
        flashcardStudyService.createFlashcardStudyByLessonIfNotExists(user.getId(),lessonId);
        return ApiResponse.<QuizstudyResponse>builder()
                .result(quizstudyService.createQuizStudy(user.getId(),lessonId,quizstudyRequest))
                .build();
    }

    @GetMapping("/lesson/{lessonId}/quiz-study/{quizstudyId}")
    public ApiResponse<QuizstudyDetailResponse> getDetailQuizStudy(@PathVariable int lessonId,
                                                                   @PathVariable int quizstudyId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        return ApiResponse.<QuizstudyDetailResponse>builder()
                .result(quizstudyService.getQuizstudyDetail(quizstudyId, user.getId(), lessonId))
                .build();
    }

//    @GetMapping("lesson/{lessonId}/quiz-study")
//    public ApiResponse<List<QuizstudyInfoResponse>> getQuizStudies(@PathVariable int lessonId) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Account user = accountService.findByUsername(username);
//
//        return ApiResponse.<List<QuizstudyInfoResponse>>builder()
//                .result(quizstudyService.getQuizStudiesByLessonId(user.getId(), lessonId))
//                .build();
//    }

    @GetMapping("lesson/{lessonId}/quiz-study")
    public ApiResponse<List<QuizstudyInfoResponse>> getQuizStudies(@PathVariable int lessonId,
                                                                   @RequestParam Map<String, String> params) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        params.put("userId", String.valueOf(user.getId()));
        params.put("lessonId", String.valueOf(lessonId));
        params.put("teststudy", "false");

        return ApiResponse.<List<QuizstudyInfoResponse>>builder()
                .result(quizstudyService.getQuizStudiesByLessonId(params))
                .build();
    }

    @DeleteMapping("/lesson/{lessonId}/quiz-study/{quizstudyId}")
    public ApiResponse<Void> deleteQuizstudy(@PathVariable int lessonId,
                                                                   @PathVariable int quizstudyId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        quizstudyService.deleteByUserAndLesson(quizstudyId, user.getId(), lessonId);
        //Chi co user thuoc cai do moi duoc xoa thuiiiiii!!!
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    //mỗi lần click đáp án là truyền api , chắc zị =))
    @PreAuthorize("@securityUtils.isQuizhistoryOwner(#id, authentication.name)")
    @PutMapping("/quiz-history/{id}")
    public ApiResponse<QuizhistoryResponse> updateQuizHistory(
            @PathVariable int id,
            @RequestBody QuizHistoryUpdateRequest request
    ) {
        return ApiResponse.<QuizhistoryResponse>builder()
                .result(quizstudyService.updateQuizHistory(id, request))
                .message("Updated successfully !!")
                .build();
    }


    //Truowngf hop lam bai kiem tra truyen danh sach user tra loi cau hoi tuong ung voi tung quiz_study so voi flashcard Id
    @PutMapping("/quiz-history-bulk")
    public ApiResponse<Void> updateBulkQuizHistory(@RequestBody List<QuizHistoryBulkUpdateRequest> requestList) {
        quizstudyService.updateQuizHistoryBulk(requestList);
        return ApiResponse.<Void>builder()
                .message("Update all completely!! ")
                .build();
    }


    //ket qua lam bai sau khi hoc quiz va lam kiem tra quiz xong



}
