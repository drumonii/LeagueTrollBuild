package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
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
 * {@link Job} configuration for retrieving the latest {@link GameMap} data from Riot's API.
 */
@Configuration
public class MapsRetrievalJobConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MapsRepository mapsRepository;

	@Bean
	public Job mapsRetrievalJob() {
		return jobBuilderFactory.get("mapsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(mapsRetrievalStep())
				.build();
	}

	@Bean
	public Step mapsRetrievalStep() {
		return stepBuilderFactory.get("mapsRetrievalStep")
				.<GameMap, GameMap> chunk(25)
				.reader(mapsRetrievalItemReader())
				.processor(mapsRetrievalItemProcessor())
				.writer(mapsRetrievalItemWriter())
				.build();
	}

	@StepScope
	@Bean
	public MapsRetrievalItemReader mapsRetrievalItemReader() {
		return new MapsRetrievalItemReader();
	}

	@Bean
	public ItemProcessor<GameMap, GameMap> mapsRetrievalItemProcessor() {
		return new MapsRetrievalItemProcessor();
	}

	@Bean
	public ItemWriter<GameMap> mapsRetrievalItemWriter() {
		RepositoryItemWriter<GameMap> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(mapsRepository);
		itemWriter.setMethodName("save");
		return itemWriter;
	}

}
