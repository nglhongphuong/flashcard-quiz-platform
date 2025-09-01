package com.zotriverse.demo.mapper;

import com.zotriverse.demo.dto.response.BookmarkResponse;
import com.zotriverse.demo.pojo.Bookmark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface BookmarkMapper {
    BookmarkResponse toBookmarkResponse(Bookmark bookmark);
}
