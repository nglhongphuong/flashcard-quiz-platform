package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.Quizanswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizanswerRepository extends JpaRepository<Quizanswer, Integer> {
}
