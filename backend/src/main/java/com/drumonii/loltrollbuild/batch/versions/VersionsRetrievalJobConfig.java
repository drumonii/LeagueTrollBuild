package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
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
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Bean
	public Job versionsRetrievalJob(JobBuilderFactory jobBuilderFactory) {
		return jobBuilderFactory.get("versionsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(versionsRetrievalStep())
				.build();
	}

	@Bean
	public Step versionsRetrievalStep() {
		return stepBuilderFactory.get("versionsRetrievalStep")
				.<Version, Version> chunk(25)
				.reader(versionsRetrievalItemReader(null))
				.processor(versionsRetrievalItemProcessor(null))
				.writer(versionsRetrievalItemWriter())
				.build();
	}

	@StepScope
	@Bean
	public VersionsRetrievalItemReader versionsRetrievalItemReader(VersionsService versionsService) {
		return new VersionsRetrievalItemReader(versionsService.getVersions());
	}

	@StepScope
	@Bean
	public VersionsRetrievalItemProcessor versionsRetrievalItemProcessor(VersionsRepository versionsRepository) {
		return new VersionsRetrievalItemProcessor(versionsRepository.latestVersion());
	}

	@Bean
	public ItemWriter<Version> versionsRetrievalItemWriter() {
		return new JdbcBatchItemWriterBuilder<Version>()
				.dataSource(dataSource)
				.sql("INSERT INTO VERSION (PATCH, MAJOR, MINOR, REVISION) VALUES (:patch, :major, :minor, :revision)")
				.beanMapped()
				.assertUpdates(false)
				.build();
	}

}
