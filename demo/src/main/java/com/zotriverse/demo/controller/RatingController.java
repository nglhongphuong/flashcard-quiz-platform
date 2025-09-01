package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.request.RatingRequest;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.LessonRatingSummaryResponse;
import com.zotriverse.demo.dto.response.RatingResponse;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.service.AccountService;
import com.zotriverse.demo.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/lesson/{lessonId}/rating")
    public ApiResponse<RatingResponse> createOrUpdate(
            @PathVariable int lessonId,
            @RequestBody RatingRequest request
            )
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        return ApiResponse.<RatingResponse>builder()
                .result(ratingService.createOrUpdateRating(user.getId(),lessonId,request))
                .build();
    }

    @DeleteMapping("/lesson/{lessonId}/rating")
    public ApiResponse<Void>deleteRating(@PathVariable int lessonId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        ratingService.deleteRating(user.getId(),lessonId);
        return ApiResponse.<Void>builder()
                .message("Deleted Successfully !!")
                .build();
    }
    @GetMapping("/lesson/{lessonId}/rating")
    public ApiResponse<LessonRatingSummaryResponse> getRatingSummary(@PathVariable int lessonId) {
        return ApiResponse.<LessonRatingSummaryResponse>builder()
                .result(ratingService.getLessonRatingSummary(lessonId))
                .build();
    }


}
