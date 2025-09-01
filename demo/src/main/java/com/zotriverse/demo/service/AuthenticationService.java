package com.zotriverse.demo.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jca.JCAContext;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.zotriverse.demo.dto.request.AuthenticationRequest;
import com.zotriverse.demo.dto.request.IntrospectRequest;
import com.zotriverse.demo.dto.request.LogoutRequest;
import com.zotriverse.demo.dto.request.RefreshRequest;
import com.zotriverse.demo.dto.response.AuthenticationResponse;
import com.zotriverse.demo.dto.response.IntrospectResponse;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.InvalidatedToken;
import com.zotriverse.demo.repository.AccountRepository;
import com.zotriverse.demo.repository.InvalidatedTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@Slf4j //log cua lomboo inject 1 logger
public class AuthenticationService {
    AccountRepository accountRepository;

    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal // ko inject vao constructor - loombok
    @Value("${SIGNER_KEY}")
    protected String SIGNER_KEY;

    @NonFinal // ko inject vao constructor - loombok
    @Value("${VALID_DURATION}") //Thoi gian het han token
    protected long VALID_DURATION;

    @NonFinal // ko inject vao constructor - loombok
    @Value("${REFRESHABLE_DURATION}") //Thoi gian thay doi token
    protected long REFRESHABLE_DURATION;


    public IntrospectResponse instrospect(IntrospectRequest request) throws JOSEException,
            ParseException {
        var token = request.getToken();
        boolean isValid = true;
       try{
           verifyToken(token,false); // chua refresh token
       }
       catch(AppException e){
          isValid = false;//Token ko hop le, true neu hop le
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user =  accountRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated =  passwordEncoder.matches(request.getPassword(),user.getPassword());
        if(!authenticated)
            throw new AppException(ErrorCode. UNAUTHENTICATED);

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    //tu thu vien nimbus
    private String generateToken(Account account){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer("zotriverse.com")// xac dinh token issure tu ai
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.HOURS).toEpochMilli() // chinh hour sau demo*
                ))//Thoi gian het han token 3 tieng
                .jwtID(UUID.randomUUID().toString()) // UUID: ma 32 bit random ngau nhien, KO BỊ TRÙNG UNIQUE
                //Them pham vi boi cac user
                .claim("scope",buildScope(account))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));//Thuat toan ky ,, kho giai ma cung nhau,
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Cannot create token" + e.getMessage(), e);
        }
    }

    private String buildScope(Account account){
        StringJoiner stringJoiner = new StringJoiner("");
        if(!account.getRole().isEmpty()){
            stringJoiner.add(account.getRole());
        }
        return stringJoiner.toString();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            //Check tgian refresh token , neu con trong  tgian refresh
            var signToken = verifyToken(request.getToken(), true); //lay token truyen tu request
            String jwt = signToken.getJWTClaimsSet().getJWTID();
            Date expiredTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jwt)
                    .expiryTime(expiredTime)
                    .build();
//Theem token con thoi han vao dataabase de kiem tra
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception){
            log.info("Token already expired!"); // token da het han
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        //Thu vien nimbus cung cap thu vien verifier
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh) //Neu True -> verofired refresh token, tgian refresh = Thoi gian issue (het han) + thoi gian refreshable
                ? new Date (signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                :signedJWT.getJWTClaimsSet().getExpirationTime(); // lay thoi gian con han cua toke

        var verified = signedJWT.verify(verifier); // tra true - verifier thanh cong /

        if(!(verified && expiryTime.after(new Date()))) // neu thay doi
        {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) // token jwtId ton tai trong database
        {
            throw new AppException(ErrorCode.UNAUTHENTICATED); // tuc la tokem da logout nhung ho lay token cu dang nhap -> ko an toan nen canh bao
        }


        // Neu thay token trong invalid - logout
        // Neu chua het expired

        return signedJWT;
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException { // refesh lai token khi het han
        //Kiem tra hieu luc token con hieu luc ko ?
        var signJWT = verifyToken(request.getToken(), true);//true vi muon refresh token
        // Cap lai token moi cho user dang su dung, dong thoi invalid token cu - het hieu luc.
        //B1: Invalid token cu
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expriryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expriryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
        //sau khi logout token cu -> isssue token moi
        //dua thong tin user generate token moi
        var username = signJWT.getJWTClaimsSet().getSubject();// Lay thong tin user tu getSubject

        var user = accountRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );
        //Buid token tu thong tin user
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }



}
