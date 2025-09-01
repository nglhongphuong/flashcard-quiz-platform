package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.Quizstudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizstudyRepository extends JpaRepository<Quizstudy, Integer> {
    @Query(value = "SELECT * FROM quizstudy " +
            "WHERE id = :quizStudyId " +
            "AND user_id = :userId " +
            "AND lesson_id = :lessonId", nativeQuery = true)


    Optional<Quizstudy> findByIdUserLesson(
            @Param("quizStudyId") int quizStudyId,
            @Param("userId") int userId,
            @Param("lessonId") int lessonId);

    @Query("SELECT q FROM Quizstudy q " +
            "WHERE q.lessonId.id = :lessonId AND q.userId.accountId = :userId")
    List<Quizstudy> findAllByLessonIdAndUserId(@Param("lessonId") int lessonId,
                                               @Param("userId") int userId);

    @Query("""
    SELECT DISTINCT fs.flashcardStudyPK.userId
    FROM FlashcardStudy fs
    JOIN fs.flashcard f
    WHERE f.lessonId.id = :lessonId
""")
    List<Integer> findUserIdsByLessonId(@Param("lessonId") int lessonId);


}
