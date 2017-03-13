package com.drumonii.loltrollbuild.batch;

import com.drumonii.loltrollbuild.riot.VersionsRetrieval;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link Job} configuration for running all retrieval jobs sequentially starting with versions.
 */
@Configuration
public class AllRetrievalsJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private VersionsRetrieval versionsRetrieval;

	@Autowired
	@Qualifier("versionsRetrievalJob")
	private Job versionsRetrievalJob;

	@Autowired
	@Qualifier("mapsRetrievalJob")
	private Job mapsRetrievalJob;

	@Autowired
	@Qualifier("summonerSpellsRetrievalJob")
	private Job summonerSpellsRetrievalJob;

	@Autowired
	@Qualifier("championsRetrievalJob")
	private Job championsRetrievalJob;

	@Autowired
	@Qualifier("itemsRetrievalJob")
	private Job itemsRetrievalJob;

	@Bean
	public Job allRetrievalsJob() {
		return jobBuilderFactory.get("allRetrievalsJob")
				.incrementer(parameters -> {
					if (parameters == null || parameters.isEmpty()) {
						return new JobParametersBuilder()
								.addLong("run.id", 1L)
								.addString("latestRiotPatch",
										versionsRetrieval.latestVersion(versionsRetrieval.versionsFromResponse()).getPatch())
								.toJobParameters();
					}
					long id = parameters.getLong("run.id", 1L) + 1;
					return new JobParametersBuilder().addLong("run.id", id).toJobParameters();
				})
				.flow(versionsRetrievalJobStep())
				.next(mapsRetrievalJobStep())
				.next(summonerSpellsRetrievalJobStep())
				.next(championsRetrievalJobStep())
				.next(itemsRetrievalJobStep())
				.end()
				.build();
	}

	@Bean
	public Step versionsRetrievalJobStep() {
		return stepBuilderFactory.get("versionsRetrievalJobStep")
				.job(versionsRetrievalJob)
				.build();
	}

	@Bean
	public Step mapsRetrievalJobStep() {
		return stepBuilderFactory.get("mapsRetrievalJobStep")
				.job(mapsRetrievalJob)
				.build();
	}

	@Bean
	public Step summonerSpellsRetrievalJobStep() {
		return stepBuilderFactory.get("summonerSpellsRetrievalJobStep")
				.job(summonerSpellsRetrievalJob)
				.build();
	}

	@Bean
	public Step championsRetrievalJobStep() {
		return stepBuilderFactory.get("championsRetrievalJobStep")
				.job(championsRetrievalJob)
				.build();
	}

	@Bean
	public Step itemsRetrievalJobStep() {
		return stepBuilderFactory.get("itemsRetrievalJobStep")
				.job(itemsRetrievalJob)
				.build();
	}

}
