package com.ss.batch.entity;

import javax.persistence.Id;
import java.io.Serializable;

public class UserGroupMappingId implements Serializable {

    private Long userGroupId;   // 사용자 그룹 id
    private String userId;      // 사용자 고유 아이디
}
