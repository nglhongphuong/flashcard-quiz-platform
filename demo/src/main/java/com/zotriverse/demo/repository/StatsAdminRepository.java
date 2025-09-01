package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Quizstudy;
import com.zotriverse.demo.pojo.Teststudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatsAdminRepository extends JpaRepository<Account, Integer> {

    @Query("SELECT a.id, a.username, a.name, a.gender, a.createdAt " +
            "FROM Account a WHERE YEAR(a.createdAt) = :year AND MONTH(a.createdAt) = :month AND a.role = 'USER'")
    List<Object[]> findUsersByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT l.id, l.title, l.userId.accountId, l.createdAt FROM Lesson l " +
            "WHERE YEAR(l.createdAt) = :year AND MONTH(l.createdAt) = :month")
    List<Object[]> findLessonsByYearAndMonth(@Param("year") int year, @Param("month") int month);

    // Lấy QuizStudy của lesson theo năm tháng
    @Query("SELECT q FROM Quizstudy q WHERE q.lessonId.id = :lessonId AND YEAR(q.createdAt) = :year AND MONTH(q.createdAt) = :month")
    List<Quizstudy> findQuizStudiesByLessonAndYearMonth(@Param("lessonId") int lessonId, @Param("year") int year, @Param("month") int month);

    // Lấy TestStudy theo lessonId năm tháng, join với Quizstudy
    @Query("SELECT t FROM Teststudy t JOIN t.quizstudy q WHERE q.lessonId.id = :lessonId AND YEAR(q.createdAt) = :year AND MONTH(q.createdAt) = :month")
    List<Teststudy> findTestStudiesByLessonAndYearMonth(@Param("lessonId") int lessonId, @Param("year") int year, @Param("month") int month);
}
