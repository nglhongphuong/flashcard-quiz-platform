package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Bookmark;
import com.zotriverse.demo.pojo.BookmarkPK;
import com.zotriverse.demo.pojo.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkPK> {
}
