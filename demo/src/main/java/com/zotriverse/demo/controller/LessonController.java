package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.request.FlashcardRequest;
import com.zotriverse.demo.dto.request.RawFlashcardRequest;
import com.zotriverse.demo.dto.response.*;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j //log cua lomboo inject 1 logger
@RequestMapping("/lesson")
@CrossOrigin
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @Autowired
    private FlashcardService flashcardService;

    @Autowired
    private FlashcardStudyService flashcardStudyService;

    @Autowired
    private ViewhistoryService viewhistoryService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    ApiResponse<LessonResponse> createLesson(@RequestParam Map<String, String> params,
                                             @RequestParam(value = "image", required = false) MultipartFile avatar)
    {
        return ApiResponse.<LessonResponse>builder()//Khởi tạo đối tượng constructor rỗng AccountResponse
                .result(lessonService.createLesson(params,avatar))//Trả về
                .build();
    }

    @GetMapping("/")
    public List<LessonResponse> getLesson(@RequestParam Map<String, String> params) {
        return lessonService.getLessons(params);
    }

    @PutMapping("/{lessonId}")
    public ApiResponse<LessonResponse> updateLesson(
            @PathVariable int lessonId,
            @RequestParam Map<String, String> params,
            @RequestParam(value = "image", required = false) MultipartFile avatar) {
        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.updateLesson(lessonId, params, avatar))
                .build();
    }

    @DeleteMapping("/{lessonId}")
    public ApiResponse<String> deleteLesson(@PathVariable int lessonId) {
        lessonService.deleteLesson(lessonId);
        return ApiResponse.<String>builder()
                .message("Delete Successfully !")
                .build();
    }
    //{lessonId}/flashcards
    @GetMapping("/{lessonId}/flashcards")
    public ApiResponse<LessonResponse> getLessonById(  @PathVariable int lessonId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();
            Optional<Account> accountOpt = Optional.ofNullable(accountService.findByUsername(username));
            if (accountOpt.isPresent()) {
                int userId = accountOpt.get().getId();
                viewhistoryService.viewLesson(userId, lessonId);
            }
        }

        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.getLessonById(lessonId))
                .build();
    }
    //==================================create flashcard by lessonId - params===============================
    @PreAuthorize("@securityUtils.isOwnerOrAdmin(#lessonId, authentication.name)")
    @PostMapping("/{lessonId}/flashcards/upload")
    public ApiResponse<List<FlashcardResponse>> uploadFlashcardFile(
            @PathVariable int lessonId,
            @RequestParam("file") MultipartFile file) {
        List<FlashcardRequest> requests = flashcardService.parseFlashcardFile(file);
        return ApiResponse.<List<FlashcardResponse>>builder()
                .result(flashcardService.createFlashcardSet(lessonId, requests))
                .build();
    }
    @PostMapping("/{lessonId}/flashcards/manual-json")
    @PreAuthorize("@securityUtils.isOwnerOrAdmin(#lessonId, authentication.name)")
    public ApiResponse<List<FlashcardResponse>> uploadFlashcardsFromJson(
            @PathVariable int lessonId,
            @RequestBody RawFlashcardRequest request) {

        List<FlashcardRequest> flashcardRequests = flashcardService.parseFromRawText(
                request.getRawText(),
                request.getCardDelimiter(),
                request.getWordDelimiter()
        );
        return ApiResponse.<List<FlashcardResponse>>builder()
                .result(flashcardService.createFlashcardSet(lessonId, flashcardRequests))
                .build();
    }

    @PostMapping("/{lessonId}/flashcards/manual")
    @PreAuthorize("@securityUtils.isOwnerOrAdmin(#lessonId, authentication.name)")
    public ApiResponse<FlashcardResponse> uploadFlashcardManual( @PathVariable int lessonId,
                                                                  @RequestParam Map<String, String> params,
                                                                  @RequestParam(value = "image", required = false) MultipartFile avatar){
        return ApiResponse.<FlashcardResponse>builder()//Khởi tạo đối tượng constructor rỗng AccountResponse
                .result(flashcardService.createFlashcard(lessonId,params,avatar))//Trả về
                .build();
    }







}
