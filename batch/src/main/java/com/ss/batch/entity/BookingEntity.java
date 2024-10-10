package com.ss.batch.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="booking")
public class BookingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingSeq;
    private Long passSeq;       // 어떤 이용권과 연결 되어있는지 예약 확인
    private String userId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;   // 예약의 상태를 관리
    private boolean usedPass;       // 이용권 사용 여부
    private boolean attended;       // 참석 여부(통계, 분석)

    private LocalDateTime startedAt; // 시작 시간
    private LocalDateTime endedAt;  // 종료 시간
    private LocalDate cancelledAt;  // 취소 시간

    // 예약한 사람의 메시지를 보내기 위해서 user 테이블과 조인
    // 여러 예약이 하나의 사용자에게 저장될 수 있음.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

}
