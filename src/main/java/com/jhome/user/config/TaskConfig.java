package com.jhome.user.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.task.configuration.DefaultTaskConfigurer;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.configuration.TaskConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@EnableTask
@Configuration
public class TaskConfig {

    private final DataSource dataSource;

    public TaskConfig(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public TaskConfigurer taskConfigurer() {
        return new DefaultTaskConfigurer(dataSource, "BOOT3_TASK_", null);
    }
}