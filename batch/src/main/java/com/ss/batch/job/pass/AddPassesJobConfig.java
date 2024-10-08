package com.ss.batch.job.pass;

// 대량 이용권을 사용자 그룹에 추가하고 발송할 때 사용하는 Job, Step 구성
// UserGroup 테이블
// Bulk_Pass 테이블
// User 테이블 필요

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddPassesJobConfig {

    // JOB을 생성하는 팩토리(클래스)를 생성함.
    private final JobBuilderFactory jobBuilderFactory;

    // STEP을 생성할 수있는 팩토리 (배치 작업의 단계)
    private final StepBuilderFactory stepBuilderFactory;

    // 실제 처리하는 내용을 가지고 있는 Tasklet 객체를 생성
    private final AddPassesTasklet addPassesTasklet;

    public AddPassesJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, AddPassesTasklet addPassesTasklet) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.addPassesTasklet = addPassesTasklet;
    }

    @Bean
    public Job addPassesJob() {
        return  this.jobBuilderFactory.get("addPassesJob").start(addPassesStep()).build();
    }

    @Bean
    public Step addPassesStep() {
        return this.stepBuilderFactory.get("addPassesStep").tasklet(new Tasklet() {

        }).build();
    }


}
