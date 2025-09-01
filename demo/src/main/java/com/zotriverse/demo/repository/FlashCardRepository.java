package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashCardRepository extends JpaRepository<Flashcard, Integer> {
    //Lấy danh sách flashcard thuộc lesson có id đuuowjc chỉ định
    @Query("SELECT f FROM Flashcard f WHERE f.lessonId.id = :lessonId")
    List<Flashcard> findByLessonId(@Param("lessonId") int lessonId);

    // ✅ Lấy flashcard theo lessonId và userId
    @Query("""
            SELECT f FROM Flashcard f 
            JOIN FlashcardStudy fs ON fs.flashcardStudyPK.flashcardId = f.id 
            WHERE f.lessonId.id = :lessonId AND fs.flashcardStudyPK.userId = :userId
            """)
    List<Flashcard> findByLessonIdAndUserId(@Param("lessonId") int lessonId, @Param("userId") int userId);

}