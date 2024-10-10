package com.ss.batch.repository;

import com.ss.batch.entity.*;
import com.ss.batch.job.pass.AddPassesTasklet;
import com.ss.batch.modelmapper.PassModelMapper;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddPassTaskletTest {

    // Spring batch
    // - 배치 작업의 각 단게에서 얼마나 많은 작업을 했는지
    //   어떤 처리가 성공햇는지 등을 기록하는 클래스
    @Mock
    private StepContribution stepContribution;

    // database
    @Mock
    private PassRepository passRepository;

    @Mock
    private BulkPassRepository bulkPassRepository;

    @Mock
    private UserGroupMappingRepository userGroupMappingRepository;

    @Mock
    private ChunkContext chunkContext;

    // 가상 객체
    @InjectMocks
    private AddPassesTasklet addPassesTasklet;


    @Test
    public void test_execute() throws Exception {

        // given
        String userGroupId = "GROUP";
        String userId = "A100001234";
        Integer count = 10;
        Long packageSeq = 1L;
        LocalDateTime now = LocalDateTime.now();

        BulkPassEntity bulkPassEntity = new BulkPassEntity();
        bulkPassEntity.setPackageSeq(1L);
        bulkPassEntity.setUserGroupId("Group");
        bulkPassEntity.setStatus(BulkPassStatus.COMPLETED);
        bulkPassEntity.setStartedAt(now.minusDays(60));
        bulkPassEntity.setCount(count);

        UserGroupMappingEntity userGroupMappingEntity = new UserGroupMappingEntity();
        userGroupMappingEntity.setUserGroupId(userGroupId);

        // when
        // - 검색했을 때 데이터가 있으면 그 데이터를 thenReturn() 메서드로 전달함.
        // - any() : 특정 타입의 인자가 무엇이든 상관없이 메서드를 실행할 수 있음. 모든 인자값을 허용
        //           지정한 타입이 맞는지 확인 후에 동작하는 설정
        when(bulkPassRepository.findByStatusAndStartedAtGreaterThan(eq(BulkPassStatus.READY), any())).thenReturn(List.of());
        when(userGroupMappingRepository.findByUserGroupId(eq(userGroupId))).thenReturn(List.of(userGroupMappingEntity));

        // 매핑 PassModelMapper 이용해서 벌크 => 패스 엔티티로 변환
        // 배치 작업 시 현재 작업에 대한 상태와 정보를 전달하는 역할로 쓰임.
        RepeatStatus repeatStatus = addPassesTasklet.execute(stepContribution, chunkContext);
        addPassesTasklet.execute(stepContribution, chunkContext);

        // then
        assertEquals(RepeatStatus.FINISHED, repeatStatus);

        // saveAll() 메서드에 값이 List 타입으로 저장
        ArgumentCaptor<List> passEntitiesCaptor = ArgumentCaptor.forClass(List.class);

        verify(passRepository, times(1)).saveAll(passEntitiesCaptor.capture());

        List<PassEntity> passEntities = passEntitiesCaptor.getValue();

        assertEquals(1, passEntities.size());

        PassEntity passEntity = passEntities.get(0);
        assertEquals(packageSeq, passEntity.getPackSeq());

        assertEquals(userId, passEntity.getUserId());
        assertEquals(PassStatus.READY, passEntity.getStatus());
        assertEquals(count, passEntity.getRemainingCount());



    }
}
