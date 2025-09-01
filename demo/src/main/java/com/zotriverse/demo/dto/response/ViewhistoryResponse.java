package com.zotriverse.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.pojo.User;
import com.zotriverse.demo.pojo.ViewhistoryPK;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewhistoryResponse {
    protected ViewhistoryPK viewhistoryPK;
    Boolean study;
    Date createdAt;
    Date updateAt;
    User user;
}
