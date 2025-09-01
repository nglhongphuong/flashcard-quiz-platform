package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.request.FlashcardRequest;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.FlashcardResponse;
import com.zotriverse.demo.dto.response.FlashcardStudyResponse;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.service.AccountService;
import com.zotriverse.demo.service.FlashcardService;
import com.zotriverse.demo.service.FlashcardStudyService;
import com.zotriverse.demo.service.LessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j //log cua lomboo inject 1 logger
@RequestMapping("/flashcard")
@CrossOrigin
public class FlashcardController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private FlashcardService flashcardService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private FlashcardStudyService flashcardStudyService;
    //create - author
    //ở lesson controller
    //create by list params ????
    //View - all
    //update - author/admin
    //delete - author/admin

    @PreAuthorize("@securityUtils.isFlashcardOwnerOrAdmin(#flashcardId, authentication.name)")
    @PutMapping("/{flashcardId}")
    public ApiResponse<FlashcardResponse> updateFlashcard(
            @PathVariable int flashcardId,
            @RequestParam Map<String, String> params,
            @RequestParam(value = "image", required = false) MultipartFile avatar) {
        FlashcardResponse updated = flashcardService.updateFlashcard(flashcardId, params, avatar);
        return ApiResponse.<FlashcardResponse>builder()
                .result(updated)
                .build();
    }
    @PreAuthorize("@securityUtils.isFlashcardOwnerOrAdmin(#flashcardId, authentication.name)")
    @DeleteMapping("/{flashcardId}")
    public ApiResponse<String> deleteFlashcard(@PathVariable int flashcardId) {
        flashcardService.deleteFlashcard(flashcardId);
        return ApiResponse.<String>builder()
                .result("Deleted Successfully !")
                .build();
    }
    @GetMapping("/{flashcardId}")
    public ApiResponse<FlashcardResponse> getFlashcardById(@PathVariable  int flashcardId)
    {
        return ApiResponse.<FlashcardResponse>builder()
                .result(flashcardService.getFlashcardById(flashcardId))
                .build();
    }



}
