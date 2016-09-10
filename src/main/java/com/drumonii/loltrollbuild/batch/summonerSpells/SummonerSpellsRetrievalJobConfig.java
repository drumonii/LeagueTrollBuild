package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
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
 * {@link Job} configuration for retrieving the latest {@link SummonerSpell} data from Riot's API.
 */
@Configuration
public class SummonerSpellsRetrievalJobConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Bean
	public Job summonerSpellsRetrievalJob() {
		return jobBuilderFactory.get("summonerSpellsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(summonerSpellsRetrievalStep())
				.build();
	}

	@Bean
	public Step summonerSpellsRetrievalStep() {
		return stepBuilderFactory.get("summonerSpellsRetrievalStep")
				.<SummonerSpell, SummonerSpell> chunk(25)
				.reader(summonerSpellsRetrievalItemReader())
				.processor(summonerSpellsRetrievalItemProcessor())
				.writer(summonerSpellsRetrievalItemWriter())
				.build();
	}

	@StepScope
	@Bean
	public SummonerSpellsRetrievalItemReader summonerSpellsRetrievalItemReader() {
		return new SummonerSpellsRetrievalItemReader();
	}

	@Bean
	public ItemProcessor<SummonerSpell, SummonerSpell> summonerSpellsRetrievalItemProcessor() {
		return new SummonerSpellsRetrievalItemProcessor();
	}

	@Bean
	public ItemWriter<SummonerSpell> summonerSpellsRetrievalItemWriter() {
		RepositoryItemWriter<SummonerSpell> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(summonerSpellsRepository);
		itemWriter.setMethodName("save");
		return itemWriter;
	}

}
