package com.ss.batch.repository;

import com.ss.batch.entity.PassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassRepository extends JpaRepository<PassEntity, Long> {
}
