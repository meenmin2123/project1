package com.ss.batch.job.pass;

import com.ss.batch.entity.*;
import com.ss.batch.modelmapper.PassModelMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AddPassesTasklet implements Tasklet {

    private final PassRepository passRepository;
    private final BulkPassRepository bulkPassRepository;
    private final UserGroupMappingRepository userGroupMappingRepository;

    public AddPassesTasklet(PassRepository passRepository, BulkPassRepository bulkPassRepository, UserGroupMappingRepository userGroupMappingRepository) {
        this.passRepository = passRepository;
        this.bulkPassRepository = bulkPassRepository;
        this.userGroupMappingRepository = userGroupMappingRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        // 이용권 시작일 1 전에 usergroup 각 사용자에게 이용권을 추가함.
        final LocalDateTime startAt = LocalDateTime.now().minusDays(1);

        // 아직 처리되지 않은 대량의 이용권 목록을 불러옴(읽기)
        List<BulkPassEntity> bulkPassEntities = bulkPassRepository.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY, startAt);

        // 각 대량 이용권 정보를 돌면서 그룹 내에 속한 사용자들을 조회하고 해당 사용자한테 이용권을 추가함.
        int count = 0;

        // user group에 속한 userId들을 조회함.
        for(BulkPassEntity bulkPassEntity : bulkPassEntities){
            List<String> userIds = userGroupMappingRepository.findByUserGroupId(bulkPassEntity.getUserGroupId())
                                      .stream().map(UserGroupMappingEntity::getUserGroupId)
                                      .collect(Collectors.toList());

            count += addPass(userIds, bulkPassEntity);
        }

        // 각 userId로 이용권을 추가하는 내용

        log.info("tasklet: execute 이용권:{}건 추가완료 startedAt: {}",count,startAt);

        return RepeatStatus.FINISHED;
    }

    // 이용권 추가하는 메서드
    public int addPass(List<String> userIds, BulkPassEntity bulkPassEntity) {

        List<PassEntity> passEntities = new ArrayList<PassEntity>();

        for(String userId : userIds){
            PassEntity passEntity = PassModelMapper.toPassEntity(userId, bulkPassEntity);
            passEntities.add(passEntity);
        }
        // 몇 건의 데이터가 추가되었는지 알기 위해서 size() 메서드 사용.
        return passRepository.saveAll(passEntities).size();
    }
}
