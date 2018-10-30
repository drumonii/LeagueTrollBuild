package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.service.ItemsService;
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
 * {@link Job} configuration for retrieving the latest {@link Item} data from Riot's API.
 */
@Configuration
public class ItemsRetrievalJobConfig {

	@Bean
	public Job itemsRetrievalJob(JobBuilderFactory jobBuilderFactory) {
		return jobBuilderFactory.get("itemsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(itemsRetrievalStep(null))
				.build();
	}

	@Bean
	public Step itemsRetrievalStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("itemsRetrievalStep")
				.<Item, Item> chunk(25)
				.reader(itemsRetrievalItemReader(null, null))
				.processor(itemsRetrievalItemProcessor(null))
				.writer(itemsRetrievalItemWriter(null))
				.build();
	}

	@StepScope
	@Bean
	public ItemsRetrievalItemReader itemsRetrievalItemReader(ItemsService itemsService,
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new ItemsRetrievalItemReader(itemsService.getItems(latestRiotPatch));
	}

	@StepScope
	@Bean
	public ItemsRetrievalItemProcessor itemsRetrievalItemProcessor(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestRiotPatch) {
		return new ItemsRetrievalItemProcessor(latestRiotPatch);
	}

	@Bean
	public ItemWriter<Item> itemsRetrievalItemWriter(ItemsRepository itemsRepository) {
		return new RepositoryItemWriterBuilder<Item>()
				.repository(itemsRepository)
				.methodName("save")
				.build();
	}

}
