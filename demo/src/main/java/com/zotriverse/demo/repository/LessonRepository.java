package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository  extends JpaRepository<Lesson, Integer> {

}