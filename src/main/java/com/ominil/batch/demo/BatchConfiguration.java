package com.ominil.batch.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final CustomerRepository customerRepository;


    BatchConfiguration(CustomerRepository repository) {
        this.customerRepository = repository;
    }

    @Bean
    public ItemReader<Customer> itemReader(CustomerRepository repository) {
        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put("id", Sort.Direction.ASC);

        return new RepositoryItemReaderBuilder<Customer>()
                .name("customerReader")
                .repository(repository)
                .methodName("getAllCustomer")
                .sorts(sort)
                .pageSize(2)
                .build();
    }

    @Bean
    ItemProcessor<Customer, String> itemProcessor() {
        return Customer::getName;
    }

    @Bean
    ItemWriter<String> itemWriter() {
        return items -> {
            for (String name: items) {
                System.out.println(name);
            }
        };
    }

    @Bean
    Job customerProcessingJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("customerProcessingJob", jobRepository)
                .flow(createStep(jobRepository, transactionManager))
                .end()
                .build();
    }

    @Bean
    Step createStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step", jobRepository)
                .<Customer, String>chunk(2, transactionManager)
                .allowStartIfComplete(true)
                .reader(itemReader(customerRepository))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }
}
