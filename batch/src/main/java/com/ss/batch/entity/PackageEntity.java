package com.ss.batch.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "package")
@Data
public class PackageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packSeq;           // 패키지 id

    private String packageName;     // 패키지 이름
    private Integer count;          // 횟수
    private Integer period;         // 기간

}
