package com.ss.batch.job.pass;

import com.ss.batch.entity.BookingEntity;
import com.ss.batch.entity.BookingStatus;
import com.ss.batch.entity.NotificationEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;

@Configuration
public class SendNotificationClassJobConfig {

    private final int CHUNK_SIZE = 10;

    // JOB을 생성하는 팩토리(클래스)를 생성한다.
    private final JobBuilderFactory jobBuilderFactory;

    // Step을 생성할 수있는 팩토리 (배치 작업의 단계)
    private final StepBuilderFactory stepBuilderFactory;

    // JPA와 데이터베이스를 연결 관리하는 객체
    private final EntityManagerFactory entityManagerFactory;

    // JPA와 데이터베이스를 연결 관리하는 객체
    private final EntityManagerFactory entityManagerFactory;

    public SendNotificationClassJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                          EntityManagerFactory entityManagerFactory) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Job sendNotificationClassJob() {
        return this.jobBuilderFactory.get("sendNotificationClassJob")
                .start()    // 첫번째 step 실행
                .next()     // 두번째 step 실행
                .build();
    }

    // 세부기능(step)
    //
    @Bean
    public Step sendNotificationClassStep() {
        return this.jobBuilderFactory.get("sendNotificationClassStep")
                .start()    // 첫번째 step 실행
                .<BookingEntity, NotificationEntity>chunk(CHUNK_SIZE)
                .reader()
                .processor()
                .writer()
                .build();
    }

    // step에서 실질적으로 데이터를 읽어오는 메소드
    @Bean
    public JpaPagingItemReader<BookingEntity> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder().name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("select b from BookingEntity b "
                            + " join b.userEntity where b.status = :status "
                            + " and b.startedAt <= :startedAt "
                            + " order by b.bookingSeq ")
                .parameterValues(Map.of("status", BookingStatus.READY, "startedAt", LocalDateTime.now().plusMinutes(10)))
                .build();
    }


}
