package com.zotriverse.demo.dto.response;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    int id;
    String username;
    String password;
    String name;
    String role;
    String gender;
    Date createdAt;
    Date updateAt;
    String avatar;
    Boolean isActive;
    LocalDate birthday;

}
