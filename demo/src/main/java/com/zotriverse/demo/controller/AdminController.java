package com.zotriverse.demo.controller;

import com.nimbusds.jose.JOSEException;
import com.zotriverse.demo.dto.request.LogoutRequest;
import com.zotriverse.demo.dto.response.AccountResponse;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.LessonResponse;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.service.AccountService;
import com.zotriverse.demo.service.LessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j //log cua lomboo inject 1 logger
@CrossOrigin
@RequestMapping("/admin/")
public class AdminController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private LessonService lessonService;

    @GetMapping("accounts")
    public List<AccountResponse> getAccounts(@RequestParam Map<String, String> params) {
        return accountService.getAccounts(params);
    }

//    @GetMapping("accounts") //Lay tat ca danh sach tai khoan phia admin
//    ApiResponse<List<AccountResponse>> getAccount() {
//        //Chua thong tin user dang nhap
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.info("Username: ", authentication.getName());
//        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));//Lay thong tin cua scope
//        return ApiResponse.<List<AccountResponse>>builder()
//                .result(accountService.getAccounts())
//                .build();
//    }


    //update 1 user bat ky
    @PutMapping("accounts/{accountId}")
    Account updateAccount(  @PathVariable(value = "accountId") int accountId,
            @RequestParam Map<String, String> params,
                          @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        return accountService.updateAccount(accountId,params, avatar);
    }

    // delete user bat ky
    @DeleteMapping("accounts/{accountId}")
    ApiResponse<String> delete( @PathVariable(value = "accountId") int accountId)
            throws ParseException, JOSEException {
        accountService.deleteAccount(accountId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PostMapping("accounts/{accountId}/lesson")
    ApiResponse<LessonResponse> createLesson(@PathVariable(value = "accountId") int accountId,
                                             @RequestParam Map<String, String> params,
                                             @RequestParam(value = "image", required = false) MultipartFile avatar)
    {
        return ApiResponse.<LessonResponse>builder()//Khởi tạo đối tượng constructor rỗng AccountResponse
                .result(lessonService.createLesson(accountId,params,avatar))//Trả về
                .build();
    }

    @GetMapping("lesson/{lessonId}")
    public ApiResponse<LessonResponse> getLessonById(  @PathVariable int lessonId){
        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.getLessonById(lessonId))
                .build();
    }


    //delete



}
