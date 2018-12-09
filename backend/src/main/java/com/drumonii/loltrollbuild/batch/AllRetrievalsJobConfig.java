package com.drumonii.loltrollbuild.batch;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import static com.drumonii.loltrollbuild.batch.scheduling.RetrievalJobsScheduling.LATEST_PATCH_KEY;

/**
 * {@link Job} configuration for running all retrieval jobs sequentially starting with versions.
 */
@Configuration
public class AllRetrievalsJobConfig {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private VersionsService versionsService;

	@Bean
	public Job allRetrievalsJob(JobBuilderFactory jobBuilderFactory) {
		return jobBuilderFactory.get("allRetrievalsJob")
				.incrementer(new AllRetrievalsJobParametersIncrementer())
				.validator(new AllRetrievalsJobParametersValidator())
				.start(allRetrievalsJobFlow())
				.end()
				.build();
	}

	@Bean
	public Flow allRetrievalsJobFlow() {
		return new FlowBuilder<SimpleFlow>("allRetrievalsJobFlow")
				.split(new SimpleAsyncTaskExecutor())
				.add(versionsRetrievalJobFlow(), mapsRetrievalJobFlow(), summonerSpellsRetrievalJobFlow(),
						championsRetrievalJobFlow(), itemsRetrievalJobFlow())
				.build();
	}

	@Bean
	public Flow versionsRetrievalJobFlow() {
		return new FlowBuilder<SimpleFlow>("versionsRetrievalJobFlow")
				.start(versionsRetrievalJobStep(null))
				.build();
	}

	@Bean
	public Step versionsRetrievalJobStep(@Qualifier("versionsRetrievalJob") Job versionsRetrievalJob) {
		return stepBuilderFactory.get("versionsRetrievalJobStep")
				.job(versionsRetrievalJob)
				.build();
	}

	@Bean
	public Flow mapsRetrievalJobFlow() {
		return new FlowBuilder<SimpleFlow>("mapsRetrievalJobFlow")
				.start(mapsRetrievalJobStep(null))
				.build();
	}

	@Bean
	public Step mapsRetrievalJobStep(@Qualifier("mapsRetrievalJob") Job mapsRetrievalJob) {
		return stepBuilderFactory.get("mapsRetrievalJobStep")
				.job(mapsRetrievalJob)
				.build();
	}

	@Bean
	public Flow summonerSpellsRetrievalJobFlow() {
		return new FlowBuilder<SimpleFlow>("summonerSpellsRetrievalJobFlow")
				.start(summonerSpellsRetrievalJobStep(null))
				.build();
	}

	@Bean
	public Step summonerSpellsRetrievalJobStep(@Qualifier("summonerSpellsRetrievalJob") Job summonerSpellsRetrievalJob) {
		return stepBuilderFactory.get("summonerSpellsRetrievalJobStep")
				.job(summonerSpellsRetrievalJob)
				.build();
	}

	@Bean
	public Flow championsRetrievalJobFlow() {
		return new FlowBuilder<SimpleFlow>("championsRetrievalJobFlow")
				.start(championsRetrievalJobStep(null))
				.build();
	}

	@Bean
	public Step championsRetrievalJobStep(@Qualifier("championsRetrievalJob") Job championsRetrievalJob) {
		return stepBuilderFactory.get("championsRetrievalJobStep")
				.job(championsRetrievalJob)
				.build();
	}

	@Bean
	public Flow itemsRetrievalJobFlow() {
		return new FlowBuilder<SimpleFlow>("itemsRetrievalJobFlow")
				.start(itemsRetrievalJobStep(null))
				.build();
	}

	@Bean
	public Step itemsRetrievalJobStep(@Qualifier("itemsRetrievalJob") Job itemsRetrievalJob) {
		return stepBuilderFactory.get("itemsRetrievalJobStep")
				.job(itemsRetrievalJob)
				.build();
	}

	private class AllRetrievalsJobParametersIncrementer implements JobParametersIncrementer {

		@Override
		public JobParameters getNext(JobParameters parameters) {
			Version latestVersion = versionsService.getLatestVersion();
			JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(parameters);
			if (latestVersion != null) {
				jobParametersBuilder.addString(LATEST_PATCH_KEY, latestVersion.getPatch());
			}
			return jobParametersBuilder.toJobParameters();
		}

	}

	private class AllRetrievalsJobParametersValidator extends DefaultJobParametersValidator {

		AllRetrievalsJobParametersValidator() {
			super(new String[] { LATEST_PATCH_KEY }, new String[0]);
		}

	}

}
