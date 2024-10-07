package com.ss.batch.etity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@
@Entity
public class BaseEntity {

    @Id
    private String id;

    // 엔티티 생성 시 실행
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 업데이트 할 때만 실행
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
