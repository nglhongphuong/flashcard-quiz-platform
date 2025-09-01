package com.zotriverse.demo.dto.request;

import com.zotriverse.demo.enums.Gender;
import com.zotriverse.demo.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor // constructor ko tham so
@AllArgsConstructor // constructor cos tham soo
@Builder

public class AccountCreationRequest {
    private String username;
    private String email;
    private String password;
    private LocalDate birthday;
    private String role;
    private String avatar;
    private Boolean isActive;
    private String gender;
}
