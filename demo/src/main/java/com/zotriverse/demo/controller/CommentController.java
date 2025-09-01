package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.request.CommentRequest;
import com.zotriverse.demo.dto.request.RawFlashcardRequest;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.CommentResponse;
import com.zotriverse.demo.dto.response.FlashcardResponse;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.service.AccountService;
import com.zotriverse.demo.service.CommentService;
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
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/lesson/{lessonId}/comment")
    public ApiResponse<CommentResponse> createComment(
            @PathVariable int lessonId,
            @RequestBody CommentRequest commentRequest)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);

        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(lessonId,user.getId(),commentRequest))
                .build();
    }

    @PreAuthorize("@securityUtils.isCommentOwnerOrAdmin(#commentId, authentication.name)")
    @PutMapping("/comment/{commentId}")
    public ApiResponse<CommentResponse> updateComment(
            @PathVariable int commentId,
            @RequestBody CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.updateComment(commentId,request))
                .build();
    }

    @PreAuthorize("@securityUtils.isCommentDeletableBy(authentication.name, #commentId)")
    @DeleteMapping("/comment/{commentId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable int commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully !!")
                .build();
    }

    @GetMapping("/lesson/{lessonId}/comment")
    public ApiResponse<List<CommentResponse>> getComments(
            @PathVariable int lessonId,
            @RequestParam Map<String, String> params) {
        return ApiResponse.<List<CommentResponse>>builder()
                .result(commentService.getComments(lessonId,params))
                .build();
    }

}
