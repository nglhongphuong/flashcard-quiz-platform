package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Flashcard;
import com.zotriverse.demo.pojo.Lessonschedule;
import com.zotriverse.demo.pojo.Viewhistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@Repository
public interface LessonscheduleRepository extends JpaRepository<Lessonschedule, Integer> {

    @Query("SELECT l FROM Lessonschedule l WHERE l.userId.id = :userId AND l.lessonId.id = :lessonId")
    List<Lessonschedule> findAllByUserIdAndLessonId(@Param("userId") Integer userId,
                                                    @Param("lessonId") Integer lessonId);
    //tìm tất cả lịch học đã đến thời điểm (trước thời gian hiện tại)
    @Query("SELECT l FROM Lessonschedule l WHERE l.scheduledTime <= :now")
    List<Lessonschedule> findAllByScheduledTimeBefore(@Param("now") Date now);

    @Query("SELECT v FROM Viewhistory v WHERE v.user.id = :userId ORDER BY v.updateAt DESC")
    List<Viewhistory> findRecentHistoryByUser(@Param("userId") Integer userId, Pageable pageable);

}
