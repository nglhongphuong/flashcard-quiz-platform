package com.zotriverse.demo.mapper;

import com.zotriverse.demo.dto.response.RatingResponse;
import com.zotriverse.demo.pojo.Rating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface RatingMapper {
    RatingResponse toRatingResponse(Rating rating);
}
