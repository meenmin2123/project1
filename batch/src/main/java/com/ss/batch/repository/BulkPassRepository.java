package com.ss.batch.repository;

import com.ss.batch.entity.BulkPassEntity;
import com.ss.batch.entity.BulkPassStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BulkPassRepository extends JpaRepository<BulkPassEntity, Long> {

    List<BulkPassEntity> findByStatusAndStartedAtGreaterThan(BulkPassStatus bulkPassStatus, LocalDateTime startAt);
}
