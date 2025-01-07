package com.jhome.user.config;

import com.jhome.user.domain.UserEntity;
import com.jhome.user.domain.UserStatus;
import com.jhome.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Slf4j
@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchConfig(JobRepository jobRepository,
                       @Qualifier("jhomeTransactionManager") PlatformTransactionManager transactionManager
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job userCleanUpJob(Step userCleanupStep) {
        return new JobBuilder("userCleanUpJob", jobRepository)
                .start(userCleanupStep)
                .build();
    }

    @Bean
    public Step userCleanupStep(ItemReader<UserEntity> userReader,
                            ItemProcessor<UserEntity,UserEntity> userProcessor,
                            ItemWriter<UserEntity> userWriter) {
        return new StepBuilder("userCleanupStep", jobRepository)
                .<UserEntity, UserEntity>chunk(2, transactionManager)
                .reader(userReader)
                .processor(userProcessor)
                .writer(userWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public RepositoryItemReader<UserEntity> userReader(UserRepository userRepository) {
        return new RepositoryItemReaderBuilder<UserEntity>()
                .name("userReader")
                .repository(userRepository)
                .methodName("findByStatus")
                .pageSize(10)
                .arguments(UserStatus.INACTIVE)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<UserEntity, UserEntity> userProcessor() {
        return item -> {
            log.info("username = {}", item.getUsername());
            return item;
        };
    }

    @Bean
    public RepositoryItemWriter<UserEntity> userWriter(UserRepository userRepository) {
        return new RepositoryItemWriterBuilder<UserEntity>()
                .repository(userRepository)
                .methodName("delete")
                .build();
    }

}