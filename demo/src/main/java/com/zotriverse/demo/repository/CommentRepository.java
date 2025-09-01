package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Comment;
import com.zotriverse.demo.pojo.Flashcard;
import com.zotriverse.demo.pojo.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // Tự động được Spring Data JPA suy luận
    Page<Comment> findByLessonId(Lesson lesson, Pageable pageable);
}
