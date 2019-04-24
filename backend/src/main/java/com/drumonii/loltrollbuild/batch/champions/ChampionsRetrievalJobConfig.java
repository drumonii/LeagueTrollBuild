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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.UniformRandomBackOffPolicy;

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
				.<Champion, Champion> chunk(25)
				.reader(championsRetrievalItemReader(null))
				.processor(championsRetrievalItemProcessor(null))
				.writer(championsRetrievalItemWriter(null))
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
	public ItemWriter<Champion> championsRetrievalItemWriter(ChampionsRepository championsRepository) {
		return new RepositoryItemWriterBuilder<Champion>()
				.repository(championsRepository)
				.methodName("save")
				.build();
	}

}
