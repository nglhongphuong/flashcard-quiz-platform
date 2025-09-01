package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Flashcard;
import com.zotriverse.demo.pojo.Rating;
import com.zotriverse.demo.pojo.RatingPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingPK> {
    // Đếm số lượng từng mức sao theo lessonId
    @Query("SELECT r.star, COUNT(r) FROM Rating r WHERE r.ratingPK.lessonId = :lessonId GROUP BY r.star")
    List<Object[]> countRatingDistribution(@Param("lessonId") int lessonId);

    @Query("SELECT AVG(CAST(r.star AS float)) FROM Rating r WHERE r.ratingPK.lessonId = :lessonId")
    Float findAverageRating(@Param("lessonId") int lessonId);
}
