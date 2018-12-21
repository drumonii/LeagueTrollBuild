package com.drumonii.loltrollbuild.test.batch;

import org.springframework.batch.core.repository.dao.*;
import org.springframework.batch.item.database.support.DataFieldMaxValueIncrementerFactory;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.batch.support.DatabaseType;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;

@TestConfiguration
public class BatchDaoTestConfiguration {

    @Bean
    public DataFieldMaxValueIncrementerFactory dataFieldMaxValueIncrementerFactory(DataSource dataSource) {
        return new DefaultDataFieldMaxValueIncrementerFactory(dataSource);
    }

    @Bean
    public JobInstanceDao jobInstanceDao(JdbcTemplate jdbcTemplate, DataSource dataSource,
            DataFieldMaxValueIncrementerFactory dataFieldMaxValueIncrementerFactory) throws
            MetaDataAccessException {
        JdbcJobInstanceDao jdbcJobInstanceDao = new JdbcJobInstanceDao();
        jdbcJobInstanceDao.setJdbcTemplate(jdbcTemplate);
        jdbcJobInstanceDao.setJobIncrementer(dataFieldMaxValueIncrementerFactory
                .getIncrementer(DatabaseType.fromMetaData(dataSource).name(), "BATCH_JOB_SEQ"));
        return jdbcJobInstanceDao;
    }

    @Bean
    public JobExecutionDao jobExecutionDao(JdbcTemplate jdbcTemplate, DataSource dataSource,
            DataFieldMaxValueIncrementerFactory dataFieldMaxValueIncrementerFactory) throws MetaDataAccessException {
        JdbcJobExecutionDao jdbcJobExecutionDao = new JdbcJobExecutionDao();
        jdbcJobExecutionDao.setJdbcTemplate(jdbcTemplate);
        jdbcJobExecutionDao.setJobExecutionIncrementer(dataFieldMaxValueIncrementerFactory
                .getIncrementer(DatabaseType.fromMetaData(dataSource).name(), "BATCH_JOB_EXECUTION_SEQ"));
        return jdbcJobExecutionDao;
    }

    @Bean
    public StepExecutionDao stepExecutionDao(JdbcTemplate jdbcTemplate, DataSource dataSource,
            DataFieldMaxValueIncrementerFactory dataFieldMaxValueIncrementerFactory) throws MetaDataAccessException {
        JdbcStepExecutionDao jdbcStepExecutionDao = new JdbcStepExecutionDao();
        jdbcStepExecutionDao.setJdbcTemplate(jdbcTemplate);
        jdbcStepExecutionDao.setStepExecutionIncrementer(dataFieldMaxValueIncrementerFactory
                .getIncrementer(DatabaseType.fromMetaData(dataSource).name(), "BATCH_STEP_EXECUTION_SEQ"));
        return jdbcStepExecutionDao;
    }

}
