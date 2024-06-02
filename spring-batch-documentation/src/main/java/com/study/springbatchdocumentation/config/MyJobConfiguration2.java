package com.study.springbatchdocumentation.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.configuration.support.JobLoader;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.JobRegistrySmartInitializingSingleton;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyJobConfiguration2 extends DefaultBatchConfiguration {

  @Bean
  public Job job(JobRepository jobRepository) {
    return new JobBuilder("job", jobRepository)
        // define job flow as needed
        .start(sampleStep())
        .build();
  }

  @Bean
  public Step sampleStep() {
    return null;
  }

  @Bean
  public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(
      JobRegistry jobRegistry) {
    JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
    postProcessor.setJobRegistry(jobRegistry);
    return postProcessor;
  }

  @Bean
  public JobRegistrySmartInitializingSingleton jobRegistrySmartInitializingSingleton(
      JobRegistry jobRegistry) {
    return new JobRegistrySmartInitializingSingleton(jobRegistry);
  }

  @Bean
  public AutomaticJobRegistrar registrar() {
    AutomaticJobRegistrar registrar = new AutomaticJobRegistrar();
    registrar.setJobLoader(jobLoader());
    registrar.setApplicationContextFactories(applicationContextFactories());
    registrar.afterPropertiesSet();
    return registrar;
  }

  @Bean
  public SimpleJobOperator jobOperator(JobExplorer jobExplorer, JobRepository jobRepository,
      JobRegistry jobRegistry, JobLauncher jobLauncher) {
    SimpleJobOperator jobOperator = new SimpleJobOperator();
    jobOperator.setJobExplorer(jobExplorer);
    jobOperator.setJobRepository(jobRepository);
    jobOperator.setJobRegistry(jobRegistry);
    jobOperator.setJobLauncher(jobLauncher);

    return jobOperator;
  }

  @Bean
  public JobLoader jobLoader() {
    return null;
  }

  @Bean
  public ApplicationContextFactory[] applicationContextFactories() {
    return null;
  }

  @Override
  protected Charset getCharset() {
    return StandardCharsets.ISO_8859_1;
  }

  @Override
  @Bean
  public JobRegistry jobRegistry() {
    return new MapJobRegistry();
  }
}
