package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.MapsRepository;
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
import org.springframework.retry.backoff.UniformRandomBackOffPolicy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Future;

/**
 * {@link Job} configuration for retrieving the latest {@link GameMap} data from Riot's API.
 */
@Configuration
public class MapsRetrievalJobConfig {

	@Bean
	public Job mapsRetrievalJob(JobBuilderFactory jobBuilderFactory) {
		return jobBuilderFactory.get("mapsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(mapsRetrievalStep(null))
				.build();
	}

	@Bean
	public Step mapsRetrievalStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("mapsRetrievalStep")
				.<GameMap, Future<GameMap>> chunk(25)
				.reader(mapsRetrievalItemReader(null))
				.processor(mapsRetrievalAsyncItemProcessor(null, null))
				.writer(mapsRetrievalAsyncItemWriter(null))
				.listener(new MapsRetrievalItemReadListener())
				.listener(new MapsRetrievalItemProcessListener())
				.listener(new MapsRetrievalItemWriteListener())
				.faultTolerant()
				.backOffPolicy(new UniformRandomBackOffPolicy())
				.skip(MapsItemRetrievalException.class)
				.skipLimit(5)
				.build();
	}

	@StepScope
	@Bean
	public MapsRetrievalItemReader mapsRetrievalItemReader(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new MapsRetrievalItemReader(latestRiotPatch);
	}

	@StepScope
	@Bean
	public MapsRetrievalItemProcessor mapsRetrievalItemProcessor(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new MapsRetrievalItemProcessor(latestRiotPatch);
	}

	@Bean
	public ItemProcessor<GameMap, Future<GameMap>> mapsRetrievalAsyncItemProcessor(
			MapsRetrievalItemProcessor mapsRetrievalItemProcessor,
			TaskExecutor mapsRetrievalTaskExecutor) {
		AsyncItemProcessor<GameMap, GameMap> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(mapsRetrievalItemProcessor);
		asyncItemProcessor.setTaskExecutor(mapsRetrievalTaskExecutor);
		return asyncItemProcessor;
	}

	@Bean
	public ItemWriter<GameMap> mapsRetrievalItemWriter(MapsRepository mapsRepository) {
		return new RepositoryItemWriterBuilder<GameMap>()
				.repository(mapsRepository)
				.build();
	}

	@Bean
	public ItemWriter<Future<GameMap>> mapsRetrievalAsyncItemWriter(
			ItemWriter<GameMap> mapsRetrievalItemWriter){
		AsyncItemWriter<GameMap> asyncItemWriter = new AsyncItemWriter<>();
		asyncItemWriter.setDelegate(mapsRetrievalItemWriter);
		return asyncItemWriter;
	}

	@Bean
	public TaskExecutor mapsRetrievalTaskExecutor(
			@Value("${batch.task-executor.core-pool-size}") int corePoolSize,
			@Value("${batch.task-executor.max-pool-size}") int maxPoolSize) {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setThreadNamePrefix("MapsRetrievalTaskExecutor-");
		return taskExecutor;
	}

}
