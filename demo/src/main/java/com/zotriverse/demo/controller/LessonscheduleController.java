package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.request.LessonscheduleRequest;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.LessonscheduleResponse;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.repository.AccountRepository;
import com.zotriverse.demo.service.AccountService;
import com.zotriverse.demo.service.LessonscheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin
public class LessonscheduleController {
    @Autowired
    private LessonscheduleService lessonscheduleService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/lesson/{lessonId}/schedule")
    public ApiResponse<LessonscheduleResponse> createSchedule(@PathVariable int lessonId,
                                                              @RequestBody LessonscheduleRequest request)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);

        return ApiResponse.<LessonscheduleResponse>builder()
                .result(lessonscheduleService.createSchedule(request,user.getId(),lessonId))
                .build();
    }
    @PutMapping("/schedule/{scheduleId}")
    public ApiResponse<LessonscheduleResponse> updateSchedule(@PathVariable int scheduleId,
                                                              @RequestBody LessonscheduleRequest request)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);

        return ApiResponse.<LessonscheduleResponse>builder()
                .result(lessonscheduleService.updateSchedule(scheduleId,request,user.getId()))
                .build();
    }

    @DeleteMapping("/schedule/{scheduleId}")
    public ApiResponse<Void> deleteSchedule(@PathVariable int scheduleId)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.findByUsername(username);
        lessonscheduleService.deleteSchedule(scheduleId,user.getId());
        return ApiResponse.<Void>builder()
                .message("Deleted Successfully !!")
                .build();
    }

}
