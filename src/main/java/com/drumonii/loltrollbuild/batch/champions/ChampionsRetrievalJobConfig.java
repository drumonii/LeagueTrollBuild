package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link Job} configuration for retrieving the latest {@link Champion} data from Riot's API.
 */
@Configuration
public class ChampionsRetrievalJobConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ChampionsRepository championsRepository;

	@Bean
	public Job championsRetrievalJob() {
		return jobBuilderFactory.get("championsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(championsRetrievalStep())
				.build();
	}

	@Bean
	public Step championsRetrievalStep() {
		return stepBuilderFactory.get("championsRetrievalStep")
				.<Champion, Champion> chunk(25)
				.reader(championsRetrievalItemReader())
				.processor(championsRetrievalItemProcessor())
				.writer(championsRetrievalItemWriter())
				.build();
	}

	@StepScope
	@Bean
	public ChampionsRetrievalItemReader championsRetrievalItemReader() {
		return new ChampionsRetrievalItemReader();
	}

	@Bean
	public ItemProcessor<Champion, Champion> championsRetrievalItemProcessor() {
		return new ChampionsRetrievalItemProcessor();
	}

	@Bean
	public ItemWriter<Champion> championsRetrievalItemWriter() {
		RepositoryItemWriter<Champion> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(championsRepository);
		itemWriter.setMethodName("save");
		return itemWriter;
	}

}
