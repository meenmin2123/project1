package com.ss.batch.repository;

import com.ss.batch.TestBatchConfig;
import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;
import com.ss.batch.job.pass.ExpiredPassJobConfig;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@SpringBatchTest
@ContextConfiguration(classes = {ExpiredPassJobConfig.class, TestBatchConfig.class})   // 테스트 할 때, 어떤 설정파일을 사용할건지 지정
public class ExpirePassJobConfigTest {

    // batch 작업을 테스트할 Job을 실행하고 그 결과를 가져오는데 사용됨.
    @Autowired
    private JobLauncherTestUtils jobLauncher;

    @Autowired
    private PassRepository passRepository;

    @Test
    public void test_expirePassStep() throws Exception {

        // given : 이미 사용 중인 이용권들을 생성
        addPassEntities(10);

        // when
        JobExecution jobExecution = jobLauncher.launchJob();

        // 위에서 실행된 job 인스턴스를 가져옴.
        JobInstance jobInstance = jobExecution.getJobInstance();

        // then
        // 두 개의 값이 같은지 확인 메서드
        assertEquals(ExitStatus.COMPLETED,jobExecution.getExitStatus());

        // 직접 설정한 job이 실행했는지 확인
        assertEquals("expiredPassJob",jobInstance.getJobName());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }
    }

    // 데이터 추가해서 만료시키는 메서드
    private void addPassEntities(int size) {
        final LocalDateTime now = LocalDateTime.now();
        final Random random = new Random();

        List<PassEntity> passEntities = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            PassEntity passEntity = new PassEntity();
            passEntity.setPackSeq(1L);
            passEntity.setUserId("A" + 10000 + i);
            passEntity.setStatus(PassStatus.PROGRESSED);
            passEntity.setRemainingCount(random.nextInt(11));
            passEntity.setStartedAt(now.minusDays(60));
            passEntity.setEndedAt(now.plusMinutes(random.nextInt(60)));
            passEntities.add(passEntity);
        }

        System.out.println(passEntities);
        List<PassEntity> result = passRepository.saveAll(passEntities);

        System.out.println("result : " + result);
    }

}
