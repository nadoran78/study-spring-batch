package com.study.springbatchdocumentation.config;

import com.study.springbatchdocumentation.component.SampleIncrementer;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  // Configuring a Job
  @Bean
  public Job footballJop(JobRepository jobRepository) {
    return new JobBuilder("footballJob", jobRepository)
        .preventRestart()  // Job restart 하는 것을 막는 메서드 : restart 하려는 경우 JobRestartException 던짐
        .listener(sampleListener()) // job이 끝나기 전 또는 후의 작업을 지정할 수 있음(아래 sampleListner 참고)
        .validator(parameterValidator()) // job 파라미터 validation 가능
        .incrementer(sampleIncrementer())
        .start(playerLoad())
        .next(gameLoad())
        .next(playerSummarization())
        .build();
  }

  @Bean
  public Job sampleJob(JobRepository jobRepository, Step sampleStep) {
    return new JobBuilder("sampleJob", jobRepository)
        .start(sampleStep)
        .build();
  }

  @Bean
  public Step sampleStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager) {
    return new StepBuilder("sampleStep", jobRepository)
        .<String, String>chunk(10, transactionManager)
        .reader(itemReader())
        .writer(itemWriter())
        .build();
  }

  @Bean
  public ItemReader<String> itemReader() {
    return null;
  }

  @Bean
  public ItemWriter<String> itemWriter() {
    return null;
  }

  @Bean
  public JobExecutionListener sampleListener() {
    return new JobExecutionListener() {
      @Override
      public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
          // job success
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
          // job failure
        }
      }
    };
  }

  @Bean
  public SampleIncrementer sampleIncrementer() {
    return new SampleIncrementer();
  }

  @Bean
  public JobParametersValidator parameterValidator() {
    return new DefaultJobParametersValidator();
  }


  @Bean
  public Step playerLoad() {
    return null;
  }

  @Bean
  public Step gameLoad() {
    return null;
  }

  @Bean
  public Step playerSummarization() {
    return null;
  }

}
