package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.LessonResponse;
import com.zotriverse.demo.dto.response.LessonViewHistoryResponse;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.service.AccountService;
import com.zotriverse.demo.service.ViewhistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin
public class ViewhistoryController {
    @Autowired
    private ViewhistoryService viewhistoryService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/view-history/recent")
    public ApiResponse<List<LessonResponse>> getViewedLessons(
            @RequestParam Map<String, String> params)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        return ApiResponse.<List<LessonResponse>>builder()
                .result(viewhistoryService.getLessonsByViewHistory(user.getId(),params))
                .build();
    }
    // getLessonViewHistoryResponse
    @GetMapping("/view-history/info")
    public ApiResponse<List<LessonViewHistoryResponse>>  getLessonViewHistoryResponse(
            @RequestParam Map<String, String> params)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        return ApiResponse.<List<LessonViewHistoryResponse>>builder()
                .result(viewhistoryService.getLessonViewHistoryResponse(user.getId(),params))
                .build();
    }





}
