package com.zotriverse.demo.mapper;

import com.zotriverse.demo.dto.response.ViewhistoryResponse;
import com.zotriverse.demo.pojo.Viewhistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface ViewhistoryMapper {
    ViewhistoryResponse toViewhistoryResponse(Viewhistory viewhistory);
}
