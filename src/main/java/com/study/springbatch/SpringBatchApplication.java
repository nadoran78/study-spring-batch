package com.study.springbatch;

import com.study.springbatch.two_minute_tutorial.HelloWorldJobConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class SpringBatchApplication {

  public static void main(String[] args) throws Exception {
    ApplicationContext context = new AnnotationConfigApplicationContext(
        HelloWorldJobConfiguration.class);
    JobLauncher jobLauncher = context.getBean(JobLauncher.class);
    Job job = context.getBean(Job.class);
    jobLauncher.run(job, new JobParameters());
  }

}
