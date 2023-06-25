package com.ominil.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	private final JobLauncher jobLauncher;
	private final Job customerProcessingJob;

	public DemoApplication(JobLauncher jobLauncher, Job customerProcessingJob) {
		this.jobLauncher = jobLauncher;
		this.customerProcessingJob = customerProcessingJob;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			JobParameters jobParameters = new JobParametersBuilder()
					.addString("JobID", String.valueOf(System.currentTimeMillis()))
					.toJobParameters();
			jobLauncher.run(customerProcessingJob, jobParameters);
		};
	}

}
