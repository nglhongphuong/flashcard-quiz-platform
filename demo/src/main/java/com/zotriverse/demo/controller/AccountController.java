package com.zotriverse.demo.controller;

import com.zotriverse.demo.dto.request.AccountCreationRequest;
import com.zotriverse.demo.dto.response.AccountResponse;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j //log cua lomboo inject 1 logger
@CrossOrigin
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @PostMapping("/create")
    Account createAccount(@RequestParam Map<String, String> params,
                          @RequestParam(value = "avatar", required = false) MultipartFile avatar){
        return accountService.createAccount(params, avatar);
    }

    @GetMapping("/info")
    ApiResponse<AccountResponse> getMyInfo() {
        return ApiResponse.<AccountResponse>builder()//Khởi tạo đối tượng constructor rỗng AccountResponse
                .result(accountService.getInfo())//Trả về
                .build();
    }
    // controller trả về định dạng dto AccountResponse -> thông qua thông tin phương thức getInfo của accountService
    // service -> lấy name xác thực tài khoản -> trả về đối tượng Account query từ findByName
    // dữ liệu Account từ service ->  MapStruct (accountMapper) để chuyển đổi từ Account (entity) → AccountResponse (DTO).
    //MapStruct sẽ tự động tạo code ánh xạ giữa Account và AccountResponse.=> Tách biệt logic Entity ↔ DTO, làm code sạch hơn.

//    Client gọi /account/info
//→ Controller gọi Service
//→ Service xác thực người dùng hiện tại
//→ Truy vấn DB lấy Account (Thông qua name của phiên đăng nhập hiện tại)
//→ Dùng Mapper để chuyển sang AccountResponse (DTO)
//→ Controller trả về cho client qua ApiResponse

    @PutMapping
    Account updateAccount(@RequestParam Map<String, String> params,
                            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        return accountService.updateAccount(params, avatar);
    }


}
