package com.ss.batch.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
// 엔티티가 생성되거나 수정될 때
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    // baseEntity
    // 테이블을 생성하지 않고 상속받은 엔티티테이블 밑에 컬럼을 가지고가서
    // 생성할 수 있도록 도와주는 추상클래스!

    // 엔티티 생성 시 실행
    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    // 업데이트 할 때만 실행
    @LastModifiedDate
    @Column(updatable = false, name = "modified_at")
    private LocalDateTime modifiedAt;
}
