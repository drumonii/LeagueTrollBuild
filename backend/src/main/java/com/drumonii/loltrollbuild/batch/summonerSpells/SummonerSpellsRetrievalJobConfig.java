package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
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
 * {@link Job} configuration for retrieving the latest {@link SummonerSpell} data from Riot's API.
 */
@Configuration
public class SummonerSpellsRetrievalJobConfig {

	@Bean
	public Job summonerSpellsRetrievalJob(JobBuilderFactory jobBuilderFactory) {
		return jobBuilderFactory.get("summonerSpellsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(summonerSpellsRetrievalStep(null))
				.build();
	}

	@Bean
	public Step summonerSpellsRetrievalStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("summonerSpellsRetrievalStep")
				.<SummonerSpell, Future<SummonerSpell>> chunk(25)
				.reader(summonerSpellsRetrievalItemReader(null))
				.processor(summonerSpellsRetrievalAsyncItemProcessor(null, null))
				.writer(summonerSpellsRetrievalAsyncItemWriter(null))
				.listener(new SummonerSpellsRetrievalItemReadListener())
				.listener(new SummonerSpellsRetrievalItemProcessListener())
				.listener(new SummonerSpellsRetrievalItemWriteListener())
				.faultTolerant()
				.backOffPolicy(new UniformRandomBackOffPolicy())
				.skip(SummonerSpellsRetrievalException.class)
				.skipLimit(5)
				.build();
	}

	@StepScope
	@Bean
	public SummonerSpellsRetrievalItemReader summonerSpellsRetrievalItemReader(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new SummonerSpellsRetrievalItemReader(latestRiotPatch);
	}

	@StepScope
	@Bean
	public SummonerSpellsRetrievalItemProcessor summonerSpellsRetrievalItemProcessor(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new SummonerSpellsRetrievalItemProcessor(latestRiotPatch);
	}

	@Bean
	public ItemProcessor<SummonerSpell, Future<SummonerSpell>> summonerSpellsRetrievalAsyncItemProcessor(
			SummonerSpellsRetrievalItemProcessor summonerSpellsRetrievalItemProcessor,
			TaskExecutor summonerSpellsRetrievalTaskExecutor) {
		AsyncItemProcessor<SummonerSpell, SummonerSpell> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(summonerSpellsRetrievalItemProcessor);
		asyncItemProcessor.setTaskExecutor(summonerSpellsRetrievalTaskExecutor);
		return asyncItemProcessor;
	}

	@Bean
	public ItemWriter<SummonerSpell> summonerSpellsRetrievalItemWriter(SummonerSpellsRepository summonerSpellsRepository) {
		return new RepositoryItemWriterBuilder<SummonerSpell>()
				.repository(summonerSpellsRepository)
				.methodName("save")
				.build();
	}

	@Bean
	public ItemWriter<Future<SummonerSpell>> summonerSpellsRetrievalAsyncItemWriter(
			ItemWriter<SummonerSpell> summonerSpellsRetrievalItemWriter){
		AsyncItemWriter<SummonerSpell> asyncItemWriter = new AsyncItemWriter<>();
		asyncItemWriter.setDelegate(summonerSpellsRetrievalItemWriter);
		return asyncItemWriter;
	}

	@Bean
	public TaskExecutor summonerSpellsRetrievalTaskExecutor(
			@Value("${batch.task-executor.core-pool-size}") int corePoolSize,
			@Value("${batch.task-executor.max-pool-size}") int maxPoolSize) {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setThreadNamePrefix("SummonerSpellsRetrievalTaskExecutor-");
		return taskExecutor;
	}

}
