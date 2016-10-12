package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link Job} configuration for retrieving the latest {@link Item} data from Riot's API.
 */
@Configuration
public class ItemsRetrievalJobConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ItemsRepository itemsRepository;

	@Bean
	public Job itemsRetrievalJob() {
		return jobBuilderFactory.get("itemsRetrievalJob")
				.incrementer(new RunIdIncrementer())
				.start(itemsRetrievalStep())
				.build();
	}

	@Bean
	public Step itemsRetrievalStep() {
		return stepBuilderFactory.get("itemsRetrievalStep")
				.<Item, Item> chunk(25)
				.reader(itemsRetrievalItemReader())
				.processor(itemsRetrievalItemProcessor(null))
				.writer(itemsRetrievalItemWriter())
				.build();
	}

	@StepScope
	@Bean
	public ItemsRetrievalItemReader itemsRetrievalItemReader() {
		return new ItemsRetrievalItemReader();
	}

	@StepScope
	@Bean
	public ItemsRetrievalItemProcessor itemsRetrievalItemProcessor(
			@Value("#{jobParameters['latestRiotPatch']}") Version latestVersion) {
		return new ItemsRetrievalItemProcessor(latestVersion);
	}

	@Bean
	public ItemWriter<Item> itemsRetrievalItemWriter() {
		RepositoryItemWriter<Item> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(itemsRepository);
		itemWriter.setMethodName("save");
		return itemWriter;
	}

}
