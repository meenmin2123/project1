package com.ss.batch.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

// 예약 테이블에서 예약 내용을 가지고 수업 전에 알림 보내는 역할로 사용
// 카톡 메시지로 보낼려면 uuid랑 Notification uuid 이용해서 전송
@Data
@Entity
@Table(name = "Notification")
public class NotificationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationSeq;   // 알람 순서
    private String uuid;            // 카카오톡
    private String event;           // 수업 전 알림을 보내기 위해서 event 발생
    private String text;            // 알림 내용
    private NotificationEvent sent;           // 발송 여부 false
    private LocalDateTime sentAt;   // 발송 시간

}
