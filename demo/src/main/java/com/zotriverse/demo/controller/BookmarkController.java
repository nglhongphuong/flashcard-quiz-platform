package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.request.BookmarkRequest;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.BookmarkResponse;
import com.zotriverse.demo.dto.response.LessonResponse;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.service.AccountService;
import com.zotriverse.demo.service.BookmarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/lesson/{lessonId}/bookmark")
    public ApiResponse<BookmarkResponse> createOrUpdate(
            @PathVariable int lessonId,
            @RequestBody BookmarkRequest request)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        return ApiResponse.<BookmarkResponse>builder()
                .result(bookmarkService.createOrDelete(user.getId(),lessonId,request))
                .build();
    }
    @GetMapping("/bookmark")
    public ApiResponse<List<LessonResponse>> getLessons(
            @RequestParam Map<String, String> params
    ){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        return ApiResponse.<List<LessonResponse>>builder()
                .result(bookmarkService.getBookmarkedLessons(user.getId(),params))
                .build();
    }

}
