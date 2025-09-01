package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Flashcard;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.Viewhistory;
import com.zotriverse.demo.pojo.ViewhistoryPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ViewhistoryRepository extends JpaRepository<Viewhistory, ViewhistoryPK> {
    @Query("SELECT vh.lesson FROM Viewhistory vh WHERE vh.viewhistoryPK.userId = :userId AND vh.study = true")
    List<Lesson> findLessonsByUserIdAndStudy(@Param("userId") int userId, @Param("study") boolean study);

    @Query("SELECT vh.lesson FROM Viewhistory vh WHERE vh.viewhistoryPK.userId = :userId ORDER BY vh.updateAt DESC")
    List<Lesson> findLessonsByUserId(@Param("userId") int userId);
}
