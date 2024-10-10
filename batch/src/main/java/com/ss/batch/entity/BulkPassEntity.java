package com.ss.batch.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "bulk_pass")
@Entity
@Data

// 대량 이용권
//  - 다수의 이용자에게 이용권을 지급
public class BulkPassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bulkPassSeq;           // 대량 이용권 순번

    private Long packageSeq;         // 패키지 순번
    private String userGroupId;         // 사용자 그룹 아이디

    @Enumerated(EnumType.STRING)
    private BulkPassStatus status;      // 상태

    private Integer count;              // 이용권 수
    private LocalDateTime startedAt;    // 시작일
    private LocalDateTime endedAt;      // 종료일
}
