package com.ss.batch.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="user_group_mapping")
@IdClass(UserGroupMappingId.class)      // 복합키 선언
public class UserGroupMappingEntity extends BaseEntity{

    @Id
    private Long userGroupId;   // 사용자 그룹 id

    @Id
    private String userId;         // 사용자 고유 아이디
    private String userGroupName;  // 사용자 그룹 명
    private String description;    // 설명

}
