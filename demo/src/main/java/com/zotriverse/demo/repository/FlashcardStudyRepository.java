package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Flashcard;
import com.zotriverse.demo.pojo.FlashcardStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlashcardStudyRepository  extends JpaRepository<FlashcardStudy, Integer> {
    @Query("""
    SELECT COUNT(fs) > 0
    FROM FlashcardStudy fs
    JOIN Flashcard f ON f.id = fs.flashcardStudyPK.flashcardId
    WHERE fs.flashcardStudyPK.userId = :userId AND f.lessonId.id = :lessonId
""")
    boolean existsByUserIdAndLessonId(@Param("userId") int userId, @Param("lessonId") int lessonId);

    @Query("""
    SELECT fs FROM FlashcardStudy fs 
    WHERE fs.flashcardStudyPK.userId = :userId 
      AND fs.flashcardStudyPK.flashcardId IN :flashcardIds
""")
    List<FlashcardStudy> findByUserIdAndFlashcardIds(@Param("userId") int userId,
                                                     @Param("flashcardIds") List<Integer> flashcardIds);

    @Query("""
    SELECT fs FROM FlashcardStudy fs 
    WHERE fs.flashcardStudyPK.userId = :userId 
      AND fs.flashcardStudyPK.flashcardId = :flashcardId
""")
    Optional<FlashcardStudy> findByUserIdAndFlashcardId(@Param("userId") int userId,
                                                        @Param("flashcardId") int flashcardId);

    @Query("""
    SELECT fs FROM FlashcardStudy fs
    WHERE fs.flashcardStudyPK.userId = :userId
      AND fs.flashcardStudyPK.flashcardId IN :flashcardIds
      AND fs.status = :status
""")
    List<FlashcardStudy> findByUserIdAndStatusAndFlashcardIds(@Param("userId") int userId,
                                                              @Param("status") String status,
                                                              @Param("flashcardIds") List<Integer> flashcardIds);

    @Query("""
    SELECT f FROM Flashcard f
    WHERE f.lessonId.id = :lessonId
""")
    List<Flashcard> findByLessonId(@Param("lessonId") int lessonId);

    @Query("""
    SELECT DISTINCT fs.flashcardStudyPK.userId
    FROM FlashcardStudy fs
    JOIN fs.flashcard f
    WHERE f.lessonId.id = :lessonId
""")
    List<Integer> findUserIdsByLessonId(@Param("lessonId") int lessonId);


}
