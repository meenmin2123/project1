package com.ss.batch.modelmapper;

// BulkPassEntity -> PassEntity로 변환

import com.ss.batch.entity.BulkPassEntity;
import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;

public class PassModelMapper {

    public static PassEntity toPassEntity (String userId, BulkPassEntity bulkPassEntity) {

        PassEntity passEntity = new PassEntity();

        Long seq = bulkPassEntity.getBulkPassSeq();
        passEntity.setPackSeq(seq);
        passEntity.setUserId(userId);
        passEntity.setStatus(PassStatus.READY);
        passEntity.setRemainingCount(bulkPassEntity.getCount());
        passEntity.setStartedAt(bulkPassEntity.getStartedAt());
        passEntity.setEndedAt(passEntity.getEndedAt());

        return passEntity;
    }
}
