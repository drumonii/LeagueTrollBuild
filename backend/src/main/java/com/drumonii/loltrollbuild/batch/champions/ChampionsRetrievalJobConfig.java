package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.service.ChampionsService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link Job} configuration for retrieving the latest {@link Champion} data from Riot's API.
 */
@Configuration
public class ChampionsRetrievalJobConfig {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ChampionsRepository championsRepository;

	@Bean
	public Job championsRetrievalJob(JobBuilderFactory jobBuilderFactory) {
		return jobBuilderFactory.get("championsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(championsRetrievalStep())
				.build();
	}

	@Bean
	public Step championsRetrievalStep() {
		return stepBuilderFactory.get("championsRetrievalStep")
				.<Champion, Champion> chunk(25)
				.reader(championsRetrievalItemReader(null, null))
				.processor(championsRetrievalItemProcessor(null))
				.writer(championsRetrievalItemWriter())
				.build();
	}

	@StepScope
	@Bean
	public ChampionsRetrievalItemReader championsRetrievalItemReader(ChampionsService championsService,
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new ChampionsRetrievalItemReader(championsService.getChampions(latestRiotPatch));
	}

	@StepScope
	@Bean
	public ChampionsRetrievalItemProcessor championsRetrievalItemProcessor(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new ChampionsRetrievalItemProcessor(latestRiotPatch);
	}

	@Bean
	public ItemWriter<Champion> championsRetrievalItemWriter() {
		return new RepositoryItemWriterBuilder<Champion>()
				.repository(championsRepository)
				.methodName("save")
				.build();
	}

}
