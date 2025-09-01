package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.Quizhistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizhistoryRepository extends JpaRepository<Quizhistory, Integer> {
    @Query("SELECT q FROM Quizhistory q WHERE q.id = :id")
    Optional<Quizhistory> findQuizhistoryById(@Param("id") int id);

    @Query("SELECT qh FROM Quizhistory qh WHERE qh.quizId.id = :quizStudyId")
    List<Quizhistory> findByQuizStudyId(@Param("quizStudyId") Integer quizStudyId);

}
