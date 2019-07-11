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
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Future;

/**
 * {@link Job} configuration for retrieving the latest {@link Version} data from Riot's API.
 */
@Configuration
public class VersionsRetrievalJobConfig {

	@Bean
	public Job versionsRetrievalJob(JobBuilderFactory jobBuilderFactory) {
		return jobBuilderFactory.get("versionsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(versionsRetrievalStep(null))
				.build();
	}

	@Bean
	public Step versionsRetrievalStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("versionsRetrievalStep")
				.<Version, Future<Version>> chunk(25)
				.reader(versionsRetrievalItemReader(null))
				.processor(versionsRetrievalAsyncItemProcessor(null, null))
				.writer(versionsRetrievalAsyncItemWriter(null))
				.listener(new VersionsRetrievalItemReadListener())
				.listener(new VersionsRetrievalItemProcessListener())
				.listener(new VersionsRetrievalItemWriteListener())
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
	public ItemProcessor<Version, Future<Version>> versionsRetrievalAsyncItemProcessor(
			VersionsRetrievalItemProcessor versionsRetrievalItemProcessor,
			TaskExecutor versionsRetrievalTaskExecutor) {
		AsyncItemProcessor<Version, Version> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(versionsRetrievalItemProcessor);
		asyncItemProcessor.setTaskExecutor(versionsRetrievalTaskExecutor);
		return asyncItemProcessor;
	}

	@Bean
	public ItemWriter<Version> versionsRetrievalItemWriter(VersionsRepository versionsRepository) {
		return new RepositoryItemWriterBuilder<Version>()
				.repository(versionsRepository)
				.methodName("save")
				.build();
	}

	@Bean
	public ItemWriter<Future<Version>> versionsRetrievalAsyncItemWriter(
			ItemWriter<Version> versionsRetrievalItemWriter){
		AsyncItemWriter<Version> asyncItemWriter = new AsyncItemWriter<>();
		asyncItemWriter.setDelegate(versionsRetrievalItemWriter);
		return asyncItemWriter;
	}

	@Bean
	public TaskExecutor versionsRetrievalTaskExecutor(
			@Value("${batch.task-executor.core-pool-size}") int corePoolSize,
			@Value("${batch.task-executor.max-pool-size}") int maxPoolSize) {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setThreadNamePrefix("VersionsRetrievalTaskExecutor-");
		return taskExecutor;
	}

}
