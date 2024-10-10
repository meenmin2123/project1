package com.ss.batch.repository;

import com.ss.batch.entity.BulkPassEntity;
import com.ss.batch.entity.BulkPassStatus;
import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;
import com.ss.batch.modelmapper.PassModelMapper;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PassModelMapperTest {

    @Test
    public void test_toPassEntity() {

        // given
        LocalDateTime now = LocalDateTime.now();
        String userID = "A10005";

        BulkPassEntity bulkPassEntity = new BulkPassEntity();
        bulkPassEntity.setPackageSeq(1L);
        bulkPassEntity.setUserGroupId("Group");
        bulkPassEntity.setStatus(BulkPassStatus.COMPLETED);
        bulkPassEntity.setStartedAt(now.minusDays(60));

        // when
        PassEntity passEntity = PassModelMapper.toPassEntity(userID, bulkPassEntity);

        // then
        assertEquals(1, passEntity);
        assertEquals(PassStatus.READY, passEntity.getStatus());
        assertEquals(now.minusDays(60), passEntity.getStartedAt());
        assertEquals(userID, passEntity.getUserId());

    }
}
