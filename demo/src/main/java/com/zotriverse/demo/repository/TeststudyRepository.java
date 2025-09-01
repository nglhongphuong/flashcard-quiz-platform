package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Quizstudy;
import com.zotriverse.demo.pojo.Teststudy;
import com.zotriverse.demo.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeststudyRepository extends JpaRepository<Teststudy, Integer> {

    @Query("""
    SELECT q FROM Quizstudy q
    JOIN FETCH q.teststudy t
    WHERE q.lessonId.id = :lessonId
    AND q.userId.accountId = :userId
    """)
    List<Quizstudy> findAllTeststudyByLessonId(@Param("lessonId") int lessonId, @Param("userId") int userId);

}
