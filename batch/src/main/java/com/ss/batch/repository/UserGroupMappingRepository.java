package com.ss.batch.repository;

import com.ss.batch.entity.UserGroupMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupMappingRepository extends JpaRepository<UserGroupMappingEntity, Long> {

    // 유저 그룹 아이디를 넣게 되면 유저 그룹의 매핑 엔티티를 가져올 수 있고 유저 아이디를 가져올 수 있도록 함.
    List<UserGroupMappingEntity> findByUserGroupId(String userGroupId);
}
