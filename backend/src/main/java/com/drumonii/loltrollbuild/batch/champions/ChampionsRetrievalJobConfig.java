package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
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
 * {@link Job} configuration for retrieving the latest {@link Champion} data from Riot's API.
 */
@Configuration
public class ChampionsRetrievalJobConfig {

	@Bean
	public Job championsRetrievalJob(JobBuilderFactory jobBuilderFactory) {
		return jobBuilderFactory.get("championsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(championsRetrievalStep(null))
				.build();
	}

	@Bean
	public Step championsRetrievalStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("championsRetrievalStep")
				.<Champion, Future<Champion>> chunk(25)
				.reader(championsRetrievalItemReader(null))
				.processor(championsRetrievalAsyncItemProcessor(null, null))
				.writer(championsRetrievalAsyncItemWriter(null))
				.listener(new ChampionsRetrievalItemReadListener())
				.listener(new ChampionsRetrievalItemProcessListener())
				.listener(new ChampionsRetrievalItemWriteListener())
				.faultTolerant()
				.backOffPolicy(new UniformRandomBackOffPolicy())
				.skip(ChampionsRetrievalException.class)
				.skipLimit(5)
				.build();
	}

	@StepScope
	@Bean
	public ChampionsRetrievalItemReader championsRetrievalItemReader(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new ChampionsRetrievalItemReader(latestRiotPatch);
	}

	@StepScope
	@Bean
	public ChampionsRetrievalItemProcessor championsRetrievalItemProcessor(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new ChampionsRetrievalItemProcessor(latestRiotPatch);
	}

	@Bean
	public ItemProcessor<Champion, Future<Champion>> championsRetrievalAsyncItemProcessor(
			ChampionsRetrievalItemProcessor championsRetrievalItemProcessor,
			TaskExecutor itemsRetrievalTaskExecutor) {
		AsyncItemProcessor<Champion, Champion> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(championsRetrievalItemProcessor);
		asyncItemProcessor.setTaskExecutor(itemsRetrievalTaskExecutor);
		return asyncItemProcessor;
	}

	@Bean
	public ItemWriter<Champion> championsRetrievalItemWriter(ChampionsRepository championsRepository) {
		return new RepositoryItemWriterBuilder<Champion>()
				.repository(championsRepository)
				.methodName("save")
				.build();
	}

	@Bean
	public ItemWriter<Future<Champion>> championsRetrievalAsyncItemWriter(
			ItemWriter<Champion> championsRetrievalItemWriter) {
		AsyncItemWriter<Champion> asyncItemWriter = new AsyncItemWriter<>();
		asyncItemWriter.setDelegate(championsRetrievalItemWriter);
		return asyncItemWriter;
	}

	@Bean
	public TaskExecutor championsRetrievalTaskExecutor(
			@Value("${batch.task-executor.core-pool-size}") int corePoolSize,
			@Value("${batch.task-executor.max-pool-size}") int maxPoolSize) {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setThreadNamePrefix("ChampionsRetrievalTaskExecutor-");
		return taskExecutor;
	}

}
