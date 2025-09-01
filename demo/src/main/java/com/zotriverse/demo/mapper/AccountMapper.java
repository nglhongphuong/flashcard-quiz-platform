package com.zotriverse.demo.mapper;
import com.zotriverse.demo.dto.request.AccountCreationRequest;
import com.zotriverse.demo.dto.response.UserInfoResponse;
import com.zotriverse.demo.pojo.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.zotriverse.demo.dto.response.AccountResponse;
import org.mapstruct.Mapping;

//Service -> mapper -> dto (request/response/entity)
//Chuyển đổi giữa các lớp Entity, DTO, Request, Response một cách nhanh gọn, không phải viết tay setter/getter.
@Mapper(componentModel = "spring") // để Spring quản lý
public interface AccountMapper {
    Account toAccount(AccountCreationRequest request);

    AccountResponse toAccountResponse(Account account);

    default UserInfoResponse toUserInfo(Account account) {
        if (account == null) return null;
        return UserInfoResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .name(account.getName())
                .avatar(account.getAvatar())
                .role(account.getRole())
                .build();
    }


}
