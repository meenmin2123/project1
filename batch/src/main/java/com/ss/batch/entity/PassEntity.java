package com.ss.batch.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pass")
@Data
// 이용권 만료를 할 때
public class PassEntity extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "pass_seq")
    private Long pass_seq;      // 이용권 순번

    @Column(name= "package_seq")
    private Long packSeq;

    @Column(name= "user_id")
    private String userId;

    // 열거형 enum 쓸 때, DB에 저장할 때는 문자열로 저장될 수 있도록 어노테이션 사용
    @Enumerated(EnumType.STRING)
    private PassStatus status;
//    private String status;

    @Column(name= "remaining_count")
    private Integer remainingCount;

    @Column(name= "started_at")
    private LocalDateTime startedAt;

    @Column(name= "ended_at")
    private LocalDateTime endedAt;

    @Column(name= "expired_at")
    private LocalDateTime expiredAt;






}
