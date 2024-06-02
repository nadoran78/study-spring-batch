package com.study.springbatchdocumentation.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

@Configuration
@EnableBatchProcessing(
    dataSourceRef = "batchDataSource",
    transactionManagerRef = "batchTransactionManager",
    tablePrefix = "BATCH_",
    maxVarCharLength = 1000,
    isolationLevelForCreate = "ISOLATION_REPEATABLE_READ")
public class MyJobConfiguration1 {

  @Bean
  public JobExplorer jobExplorer() throws Exception {
    JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();
    factoryBean.setDataSource(batchDataSource());
    factoryBean.setTablePrefix("SYSTEM.");
    return factoryBean.getObject();
  }

  @Bean
  public TransactionProxyFactoryBean baseProxy() throws Exception {
    TransactionProxyFactoryBean transactionProxyFactoryBean = new TransactionProxyFactoryBean();
    Properties transactionAttributes = new Properties();
    transactionAttributes.setProperty("*", "PROPAGATION_REQUIRED");
    transactionProxyFactoryBean.setTransactionAttributes(transactionAttributes);
    transactionProxyFactoryBean.setTarget(jobRepository());
    transactionProxyFactoryBean.setTransactionManager(batchTransactionManager(batchDataSource()));
    return transactionProxyFactoryBean;
  }

  @Bean
  public JobLauncher jobLauncher() throws Exception {
    TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
    jobLauncher.setJobRepository(jobRepository());
    jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }

  @Bean
  public JobRepository jobRepository() throws Exception {
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(batchDataSource());
    factory.setDatabaseType("db2");
    factory.setTransactionManager(batchTransactionManager(batchDataSource()));
    return factory.getObject();
  }

  @Bean
  public DataSource batchDataSource() {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
        .addScript("/org/springframework/batch/core/schema-hsql.sql")
        .generateUniqueName(true).build();
  }

  @Bean
  public JdbcTransactionManager batchTransactionManager(DataSource dataSource) {
    return new JdbcTransactionManager(dataSource);
  }

  @Bean
  public Job job(JobRepository jobRepository) {
    return new JobBuilder("myJob", jobRepository)
        .start(sampleStep())
        // define job flow as needed
        .build();
  }

  @Bean
  public Step sampleStep() {
    return null;
  }
}
