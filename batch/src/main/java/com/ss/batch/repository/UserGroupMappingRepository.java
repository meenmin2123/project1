package com.ss.batch.repository;

import com.ss.batch.entity.UserGroupMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupMappingRepository extends JpaRepository<UserGroupMappingEntity, Long> {
}
