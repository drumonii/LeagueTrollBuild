package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * {@link Job} configuration for retrieving the latest {@link Version} data from Riot's API.
 */
@Configuration
public class VersionsRetrievalJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Bean
	public Job versionsRetrievalJob() {
		return jobBuilderFactory.get("versionsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(versionsRetrievalStep())
				.build();
	}

	@Bean
	public Step versionsRetrievalStep() {
		return stepBuilderFactory.get("versionsRetrievalStep")
				.<Version, Version> chunk(25)
				.reader(versionsRetrievalItemReader())
				.processor(versionsRetrievalItemProcessor())
				.writer(versionsRetrievalItemWriter())
				.build();
	}

	@StepScope
	@Bean
	public VersionsRetrievalItemReader versionsRetrievalItemReader() {
		return new VersionsRetrievalItemReader();
	}

	@Bean
	public ItemProcessor<Version, Version> versionsRetrievalItemProcessor() {
		return new VersionsRetrievalItemProcessor();
	}

	@Bean
	public ItemWriter<Version> versionsRetrievalItemWriter() {
		JdbcBatchItemWriter<Version> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setSql(
				"INSERT INTO VERSION (PATCH, MAJOR, MINOR, REVISION) VALUES (:patch, :major, :minor, :revision)"
		);
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		itemWriter.setAssertUpdates(false);
		return itemWriter;
	}

}
