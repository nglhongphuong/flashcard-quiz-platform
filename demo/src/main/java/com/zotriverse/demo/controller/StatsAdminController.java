package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.response.stats.StatsAllResponse;
import com.zotriverse.demo.dto.response.stats.StatsLessonResponse;
import com.zotriverse.demo.service.StatsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin")
public class StatsAdminController {

    @Autowired
    private StatsAdminService service;

    @GetMapping("/stats/{year}")
    public StatsAllResponse getStatsAll(@PathVariable(required = false) Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        return service.statsAll(year);
    }

    @GetMapping("/lesson/{lessonId}/stats/{year}")
    public StatsLessonResponse getStatsLesson(@PathVariable int lessonId,
                                              @PathVariable(required = false) Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        return service.statsLesson(lessonId, year);
    }
}
