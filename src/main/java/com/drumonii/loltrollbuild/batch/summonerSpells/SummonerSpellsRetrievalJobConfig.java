package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.service.SummonerSpellsService;
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
 * {@link Job} configuration for retrieving the latest {@link SummonerSpell} data from Riot's API.
 */
@Configuration
public class SummonerSpellsRetrievalJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

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
				.reader(summonerSpellsRetrievalItemReader(null))
				.processor(summonerSpellsRetrievalItemProcessor(null))
				.writer(summonerSpellsRetrievalItemWriter())
				.build();
	}

	@StepScope
	@Bean
	public SummonerSpellsRetrievalItemReader summonerSpellsRetrievalItemReader(SummonerSpellsService summonerSpellsService) {
		return new SummonerSpellsRetrievalItemReader(summonerSpellsService.getSummonerSpells());
	}

	@StepScope
	@Bean
	public SummonerSpellsRetrievalItemProcessor summonerSpellsRetrievalItemProcessor(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new SummonerSpellsRetrievalItemProcessor(latestRiotPatch);
	}

	@Bean
	public ItemWriter<SummonerSpell> summonerSpellsRetrievalItemWriter() {
		return new RepositoryItemWriterBuilder<SummonerSpell>()
				.repository(summonerSpellsRepository)
				.methodName("save")
				.build();
	}

}
