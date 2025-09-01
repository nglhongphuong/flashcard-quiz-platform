package com.zotriverse.demo.configuration;

import com.zotriverse.demo.enums.Gender;
import com.zotriverse.demo.enums.Role;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Admin;
import com.zotriverse.demo.pojo.User;
import com.zotriverse.demo.repository.AccountRepository;
import com.zotriverse.demo.repository.AdminRepository;
import jakarta.ws.rs.core.Application;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDate;
import java.util.HashSet;
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j //log cua lomboo inject 1 logger
@EnableTransactionManagement
@EnableScheduling //Cai dat thiet lap thoi gian de goi job phien lam viec
public class ApplicationInit {

    BCryptPasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "123456";

    //Chay dau khi chuong trinh chay
    @Bean
    ApplicationRunner applicationRunner(AccountRepository accountRepository, AdminRepository adminRepository){
        return args -> {
            //Kiem tra admin da ton tai chua, neu chua thi tao account admin mac dinh cua he thong
            if(accountRepository.findByUsername("admin").isEmpty()){
                Account account =  Account.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .role(Role.ADMIN.name())
                        .gender(Gender.MALE.name())
                        .birthday(LocalDate.of(2000, 1, 1))
                        .name("admin")
                        .build();

                accountRepository.save(account);

                Account acc = accountRepository.findByUsername(account.getUsername())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                Admin admin = Admin.builder()
                        .accountId(acc.getId())
                        .build();

                adminRepository.save(admin);


                log.warn("Admin user has been created with default password: 123456, please change !");
            }
        };
    }
}
