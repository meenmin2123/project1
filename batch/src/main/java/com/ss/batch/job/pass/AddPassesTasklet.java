package com.ss.batch.job.pass;

import com.ss.batch.entity.BulkPassEntity;
import com.ss.batch.entity.BulkPassStatus;
import com.ss.batch.repository.BulkPassRepository;
import com.ss.batch.repository.PassRepository;
import com.ss.batch.repository.UserGroupMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class AddPassesTasklet implements Tasklet {

    private final PassRepository passRepository;
    private final BulkPassRepository bulkPassRepository;
    private final UserGroupMappingRepository userGroupMappingRepository;

    public AddPassesTasklet(PassRepository, passRepository,userGroupMappingRepository);


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        // 이용권 시작일 1 전에 usergroup 각 사용자에게 이용권을 추가함.
        final LocalDateTime startAt = LocalDateTime.now().minusDays(1);

        // 아직 처리되지 않은 대량의 이용권 목록을 불러옴(읽기)
        List<BulkPassEntity> bulkPassEntities = bulkPassRepository.findByStatusbAndStartedAtGreaterThan(BulkPassStatus.READY);

        return null;
    }
}
