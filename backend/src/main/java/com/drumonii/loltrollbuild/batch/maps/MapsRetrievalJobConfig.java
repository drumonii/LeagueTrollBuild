package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.service.MapsService;
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
				.<GameMap, GameMap> chunk(25)
				.reader(mapsRetrievalItemReader(null, null))
				.processor(mapsRetrievalItemProcessor(null))
				.writer(mapsRetrievalItemWriter(null))
				.build();
	}

	@StepScope
	@Bean
	public MapsRetrievalItemReader mapsRetrievalItemReader(MapsService mapsService,
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new MapsRetrievalItemReader(mapsService.getMaps(latestRiotPatch));
	}

	@StepScope
	@Bean
	public MapsRetrievalItemProcessor mapsRetrievalItemProcessor(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new MapsRetrievalItemProcessor(latestRiotPatch);
	}

	@Bean
	public ItemWriter<GameMap> mapsRetrievalItemWriter(MapsRepository mapsRepository) {
		return new RepositoryItemWriterBuilder<GameMap>()
				.repository(mapsRepository)
				.methodName("save")
				.build();
	}

}
