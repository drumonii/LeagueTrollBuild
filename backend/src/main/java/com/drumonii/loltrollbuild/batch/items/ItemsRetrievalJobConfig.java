package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
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
 * {@link Job} configuration for retrieving the latest {@link Item} data from Riot's API.
 */
@Configuration
public class ItemsRetrievalJobConfig {

	@Bean
	public Job itemsRetrievalJob(JobBuilderFactory jobBuilderFactory) {
		return jobBuilderFactory.get("itemsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(itemsRetrievalStep(null))
				.build();
	}

	@Bean
	public Step itemsRetrievalStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("itemsRetrievalStep")
				.<Item, Future<Item>> chunk(25)
				.reader(itemsRetrievalItemReader(null))
				.processor(itemsRetrievalAsyncItemProcessor(null, null))
				.writer(itemsRetrievalAsyncItemWriter(null))
				.listener(new ItemsRetrievalItemReadListener())
				.listener(new ItemsRetrievalItemProcessListener())
				.listener(new ItemsRetrievalItemWriteListener())
				.faultTolerant()
				.backOffPolicy(new UniformRandomBackOffPolicy())
				.skip(ItemsRetrievalException.class)
				.skipLimit(5)
				.build();
	}

	@StepScope
	@Bean
	public ItemsRetrievalItemReader itemsRetrievalItemReader(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new ItemsRetrievalItemReader(latestRiotPatch);
	}

	@StepScope
	@Bean
	public ItemsRetrievalItemProcessor itemsRetrievalItemProcessor(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new ItemsRetrievalItemProcessor(latestRiotPatch);
	}

	@Bean
	public ItemProcessor<Item, Future<Item>> itemsRetrievalAsyncItemProcessor(
			ItemsRetrievalItemProcessor itemsRetrievalItemProcessor,
			TaskExecutor itemsRetrievalTaskExecutor) {
		AsyncItemProcessor<Item, Item> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(itemsRetrievalItemProcessor);
		asyncItemProcessor.setTaskExecutor(itemsRetrievalTaskExecutor);
		return asyncItemProcessor;
	}

	@Bean
	public ItemWriter<Item> itemsRetrievalItemWriter(ItemsRepository itemsRepository) {
		return new RepositoryItemWriterBuilder<Item>()
				.repository(itemsRepository)
				.methodName("save")
				.build();
	}

	@Bean
	public ItemWriter<Future<Item>> itemsRetrievalAsyncItemWriter(
			ItemWriter<Item> itemsRetrievalItemWriter){
		AsyncItemWriter<Item> asyncItemWriter = new AsyncItemWriter<>();
		asyncItemWriter.setDelegate(itemsRetrievalItemWriter);
		return asyncItemWriter;
	}

	@Bean
	public TaskExecutor itemsRetrievalTaskExecutor(
			@Value("${batch.task-executor.core-pool-size}") int corePoolSize,
			@Value("${batch.task-executor.max-pool-size}") int maxPoolSize) {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setThreadNamePrefix("ItemsRetrievalTaskExecutor-");
		return taskExecutor;
	}

}
