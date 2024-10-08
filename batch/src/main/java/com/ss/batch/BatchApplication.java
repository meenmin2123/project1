package com.ss.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class BatchApplication {

	// 생성자 주입방식


	// JOB을 생성하는 factory
	private final JobBuilderFactory jobBuilderFactory;

	// Step을 생성할 수 있는 factory
	// final 말고 @Autowrid으로 변경해도 됨.
	private final StepBuilderFactory stepBuilderFactory;

	public BatchApplication(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public JobBuilderFactory jobBuilderFactory() {
		return jobBuilderFactory;
	}

	@Bean
	public StepBuilderFactory stepBuilderFactory() {
		return stepBuilderFactory;
	}

	@Bean
	public Step passStep() {
		return this.stepBuilderFactory.get("passStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Execute PassStep");
				return RepeatStatus.FINISHED;
			}
		}).build();

	}

	@Bean Job passJob() {
		return this.jobBuilderFactory.get("passJob")
				.incrementer(new RunIdIncrementer()) 	// 자동으로 고유한 아이디값을 생성
				.start(passStep()) 						// step 을 넣어서 처리할 수있또록!
				.build();
	}

	// incrementer() 고유한 아디를 생성하는 설정 메서드
	// 다른 id를 job 실행 시 생성해서 저장하는 역하ㅏㅏㄹ
	// new RunIdIncrementer()


	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

}

//*    이용권을 기준으로 해서 데이터 설계
// *     여러개의 헬스장에서 이 프로그램을 사용할 수있도록 !
//		*     N개의 체육관을 기준으로 해서 테이블생성
// *
//		 *    체육관
// *     체육관ID , 이름, 장소,상태
// *
//		 *    사용자
// *     사용자 ID, 이름, 전화번호 ,상태
// *
//		 *    패키지
// *     체육관ID , 패키지ID , 패키지ID, 기간, 횟수
// *
//		 *    이용권
// *      이용권순번, 체육관ID,사용자ID,패키지ID,잔여횟수.
// *      시작 일시, 종료일시
// *
//		 *    예약
// *     예약순번,체육관 ID,사용자ID,이용권순번,시작일시,종료일시
// *     이용권 차감 여부!
//		*
//		*
