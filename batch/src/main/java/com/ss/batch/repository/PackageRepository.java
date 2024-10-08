package com.ss.batch.repository;

import com.ss.batch.entity.PackageEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {

    List<PackageEntity> findByCreatedAtAfter(LocalDateTime dateTime, PageRequest page);

    @Modifying
    @Transactional
    @Query(value = "update PackageEntity p set p.count = :count, p.period = :period where p.packSeq = :packSeq")
    int updateCountAndPeriod(@Param("packSeq")Long packSeq, Integer count, Integer period);
}
