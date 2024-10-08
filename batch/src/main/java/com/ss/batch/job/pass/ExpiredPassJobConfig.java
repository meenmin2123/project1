package com.ss.batch.job.pass;

import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;

// 이용권이 만료 되었을 때 배치 작업을 설정하는 클래스
@Configuration
public class ExpiredPassJobConfig {

    // 데이터를 한꺼번에 처리할 수 있는 사이즈
    private final int CHUNK_SIZE = 5;

    // JOB을 생성하는 팩토리(클래스)를 생성함.
    private final JobBuilderFactory jobBuilderFactory;

    // STEP을 생성할 수있는 팩토리 (배치 작업의 단계)
    private final StepBuilderFactory stepBuilderFactory;

    // JPA와 데이터베이스를 연결 관리하는 객체
    private final EntityManagerFactory entityManagerFactory;

    public ExpiredPassJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Job expiredPassJob() {
        return this.jobBuilderFactory.get("expiredPassJob")       // 배치 작성을 생성해서 이름을 저장
                                     .start(expiredPassStep())    // step을 실행하는 메서드
                                     .build();                    // job 생성함
    }

    // step
    @Bean
    public Step expiredPassStep() {

        // <PassEntity,PassEntity> : <입력, 출력> 데이터 타입
        // - 데이터베이스에서 데이터를 읽어 올 때 타입
        // - 첫번째 제네릭 타입 : 데이터베이스에서 데이터를 읽어 올 때 타입
        // - 두번째 제네릭 타입 : 데이터베이스에서 데이터를 처리하거나 불러올 때 타입 수정된 데이터나 추가된 데이터를 저장
        return this.stepBuilderFactory.get("expiredPassStep").<PassEntity,PassEntity>chunk(CHUNK_SIZE)
                                                             .reader(expiredPassItemReader())       // 데이터 읽어오기
                                                             .processor(expiredPassItemProcessor())    // 데이터 처리하기
                                                             .writer(expiredPassItemWriter())       // 저장
                                                             .build();
    }

    @Bean
    @StepScope  //step 실행될 때마다
    public JpaCursorItemReader<PassEntity> expiredPassItemReader() {

        return new JpaCursorItemReaderBuilder<PassEntity>().name("expiredPassItemReader")   // ItemReader 여러 개 중
                                                           .entityManagerFactory(entityManagerFactory)          // JPA를 통해서 데이터베이스에 연결하고 데이터베이스 관리
                                                            // 상태(status)가 진행 중이며, 중요일시(endedAt)이 현재 시점보다 과거일 만료 대상
                                                            // JPQL 쿼리
                                                           .queryString("select p from PassEntity p where p.status = :status and p.endedAt <= :endedAt")
                                                           .parameterValues(Map.of("status", PassStatus.PROGRESSED, "endedAt", LocalDateTime.now()))
                                                           .build();
    }

    @Bean
    public ItemProcessor<PassEntity, PassEntity> expiredPassItemProcessor() {

        // 인터페이스를 이용해서 itemprocessor 생성
        // 익명 클래스를 이용해서 사용함. 람다식으로 표현
        return new ItemProcessor<PassEntity, PassEntity>() {

            @Override
            public PassEntity process(PassEntity item) throws Exception {
                // 실제 처리하는 내용
                // 상태 현재 이용 중에서 만료
                // 만료 일자도 현재 날짜를 기준으로 수정
                item.setStatus(PassStatus.PROGRESSED);

                return item;
            }
        };

        // 람다식 표현
//        return passEntity -> {
//            passEntity.setStatus(PassStatus.EXPIRED);
//            passEntity.setExpiredAt(LocalDateTime.now());
//            return passEntity;
//        };
    }

    @Bean
    public JpaItemWriter<PassEntity> expiredPassItemWriter() {

        JpaItemWriter<PassEntity> jpaItemWriter = new JpaItemWriter<PassEntity>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}


