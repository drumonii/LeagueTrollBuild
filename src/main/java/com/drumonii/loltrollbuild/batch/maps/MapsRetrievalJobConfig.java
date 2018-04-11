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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link Job} configuration for retrieving the latest {@link GameMap} data from Riot's API.
 */
@Configuration
public class MapsRetrievalJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

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
				.reader(mapsRetrievalItemReader(null))
				.processor(mapsRetrievalItemProcessor(null))
				.writer(mapsRetrievalItemWriter())
				.build();
	}

	@StepScope
	@Bean
	public MapsRetrievalItemReader mapsRetrievalItemReader(MapsService mapsService) {
		return new MapsRetrievalItemReader(mapsService.getMaps());
	}

	@StepScope
	@Bean
	public MapsRetrievalItemProcessor mapsRetrievalItemProcessor(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new MapsRetrievalItemProcessor(latestRiotPatch);
	}

	@Bean
	public ItemWriter<GameMap> mapsRetrievalItemWriter() {
		return new RepositoryItemWriterBuilder<GameMap>()
				.repository(mapsRepository)
				.methodName("save")
				.build();
	}

}
