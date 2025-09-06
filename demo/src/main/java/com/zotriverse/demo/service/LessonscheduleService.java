package com.zotriverse.demo.service;

import com.zotriverse.demo.dto.request.LessonscheduleRequest;
import com.zotriverse.demo.dto.response.LessonResponse;
import com.zotriverse.demo.dto.response.LessonscheduleResponse;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.mapper.LessonMapper;
import com.zotriverse.demo.mapper.LessonscheduleMapper;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.Lessonschedule;
import com.zotriverse.demo.pojo.Viewhistory;
import com.zotriverse.demo.repository.AccountRepository;
import com.zotriverse.demo.repository.LessonRepository;
import com.zotriverse.demo.repository.LessonscheduleRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j //log cua lomboo inject 1 logger
public class LessonscheduleService {
    @Autowired
    private LessonscheduleRepository lessonscheduleRepository;

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LessonscheduleMapper lessonscheduleMapper;

    @Autowired
    public JavaMailSender mailSender;

    public void sendReminderEmail(String toEmail, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true); //tra vetrue nếu dùng HTML
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gửi email: " + e.getMessage(), e);
        }
    }

    public LessonscheduleResponse createSchedule(LessonscheduleRequest request, Integer userId, Integer lessonId) {
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

        Lessonschedule schedule = Lessonschedule.builder()
                .scheduledTime(request.getScheduledTime())
                .notice(request.getNotice() != null ? request.getNotice() : "Nhắc nhở học bài theo lịch đã lên.")
                .lessonId(lesson)
                .userId(user.getUser())
                .build();
        lessonscheduleRepository.save(schedule);
       return lessonscheduleMapper.toLessonscheduleResponse(schedule);
    }
    public void deleteSchedule(Integer scheduleId, Integer userId) {
        Lessonschedule schedule = lessonscheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        if (!schedule.getUserId().getAccount().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        lessonscheduleRepository.delete(schedule);
        log.info("Đã xóa lịch học id={} của userId={}", scheduleId, userId);
    }

    public LessonscheduleResponse updateSchedule(Integer scheduleId, LessonscheduleRequest request, Integer userId) {
        Lessonschedule schedule = lessonscheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        if (!schedule.getUserId().getAccount().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        schedule.setScheduledTime(request.getScheduledTime());
        schedule.setNotice(request.getNotice());
        lessonscheduleRepository.save(schedule);
        return lessonscheduleMapper.toLessonscheduleResponse(schedule);
    }

    @Scheduled(cron = "0 * * * * *") // mỗi phút
    @Transactional
    public void sendScheduleReminders() {
        Date now = new Date();
        List<Lessonschedule> schedules = lessonscheduleRepository
                .findAllByScheduledTimeBefore(now);
        if (schedules.isEmpty()) {
            return;
        }
        for (Lessonschedule schedule : schedules) {
            String to = schedule.getUserId().getAccount().getUsername();//username chinh la mail
            String subject = "Bạn có lịch nhắc nhở ôn tập: " + schedule.getLessonId().getTitle();
            String content = schedule.getNotice();

            try {
                this.sendReminderEmail(to, subject, content);
                lessonscheduleRepository.delete(schedule); //xoas sau khi gui xong
                log.info("Đã gửi và xóa lịch học cho user co email ={}, scheduleId={}", schedule.getUserId().getAccount().getUsername(), schedule.getId());
            } catch (Exception e) {
                log.error("Không gửi được email cho user co email={}, scheduleId={}, error={}",
                        schedule.getUserId().getAccount().getUsername(), schedule.getId(), e.getMessage());
            }
        }
    }


    @Scheduled(cron = "0 0 9 * * ?") // 9h sáng mỗi ngày 0 0 9
    @Transactional
    public void sendAutoReminders() {
        Date now = new Date();
        Date threeDaysAgo = Date.from(LocalDate.now().minusDays(3)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        // Query user mà lần học cuối < 3 ngày
        List<Account> inactiveUsers = accountRepository.findUsersInactiveSince(threeDaysAgo);

        for (Account user : inactiveUsers) {
            // lấy danh sách bài học gần đây (limit 3)
            List<Viewhistory> recentLessons = lessonscheduleRepository.findRecentHistoryByUser(user.getId(), PageRequest.of(0, 3));

            String lessonList = recentLessons.stream()
                    .map(v -> "- " + v.getLesson().getTitle())
                    .collect(Collectors.joining("<br/>"));

            String subject = "📩 Nhắc nhở học tập - Đã 3 ngày rồi bạn chưa quay lại!";

            String content = """
<div style="font-family: Arial, sans-serif; max-width:600px; margin:auto; border:1px solid #e0e0e0; border-radius:10px; padding:20px; background-color:#ffffff;">
    
    <!-- Header -->
    <div style="text-align:center; background:#A8D5BA; padding:15px; border-radius:10px 10px 0 0; color:#fff;">
        <h2 style="margin:0;">🌱 Trở lại học tập nào!</h2>
    </div>

    <!-- Body -->
    <div style="padding:20px; color:#333;">
        <p>Xin chào <b>%s</b>,</p>
        <p>Đã <b>3 ngày</b> rồi bạn chưa ôn lại kiến thức. Hãy quay lại và tiếp tục chặng đường học tập nhé ✨.</p>
        
        <p><b>Các học phần bạn đã học gần đây:</b></p>
        <div style="background:#F0FDF4; padding:10px 15px; border-left:4px solid #A8D5BA; border-radius:5px; line-height:1.6;">
            %s
        </div>

        <p style="margin-top:20px; text-align:center;">
             <a href="https://zotriverse.onrender.com/" 
                 style="background:#A8D5BA; color:#fff; padding:12px 20px; text-decoration:none; border-radius:5px; font-weight:bold; display:inline-block;">
                Quay lại học ngay
            </a>
        </p>

        <p style="font-size:13px; color:#777;">Đừng bỏ cuộc nha, chúng tôi tin bạn làm được! 🚀</p>
    </div>

    <!-- Footer -->
    <div style="text-align:center; font-size:12px; color:#aaa; margin-top:20px; border-top:1px solid #eee; padding-top:10px;">
        © 2025 Zotriverse Learning Platform
    </div>
</div>
""".formatted(user.getName(), lessonList);



            sendReminderEmail(user.getUsername(), subject, content);
        }
    }

}
