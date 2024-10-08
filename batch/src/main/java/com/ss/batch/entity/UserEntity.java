package com.ss.batch.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Map;

@Table(name="user")
@Data
@Entity
public class UserEntity extends BaseEntity{

    @Id
    private String userId;

    private String userName;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
//    private String status;      // 사용자 상태

    private String phone;

    // Json 형태로 저장된 데이터를 자바에서 사용하기 위한 구조
    @Type(type="json")
    private Map<String, Object> metadata;   // 세부정보

    // 후반에 일괄적으로 고객한테 배포하기 위해서 카카오톡 메시지 보낼 때
    // UUID를 메타 데이터에서 추출해서 저장.




}
