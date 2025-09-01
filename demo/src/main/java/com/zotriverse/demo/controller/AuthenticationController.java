package com.zotriverse.demo.controller;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.zotriverse.demo.dto.request.AuthenticationRequest;
import com.zotriverse.demo.dto.request.IntrospectRequest;
import com.zotriverse.demo.dto.request.LogoutRequest;
import com.zotriverse.demo.dto.request.RefreshRequest;
import com.zotriverse.demo.dto.response.ApiResponse;
import com.zotriverse.demo.dto.response.AuthenticationResponse;
import com.zotriverse.demo.dto.response.IntrospectResponse;
import com.zotriverse.demo.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@CrossOrigin
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/login")//tra token
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)// tạo đối tượng
                .build();
    }

    @PostMapping("/introspect")//verified xac dinh token con han ko
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.instrospect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)// tạo đối tượng
                .build();
    }

    @PostMapping("/logout")// dang xuat de danh dau token het han khi da logout
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
          authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh") //refresh khi token het han 3 tieng trong qua trinh user su dung
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }





}
