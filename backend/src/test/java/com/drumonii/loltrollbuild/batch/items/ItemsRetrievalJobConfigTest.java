package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.ImageFetcher;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.service.ItemsService;
import com.drumonii.loltrollbuild.test.batch.AbstractBatchTests;
import com.drumonii.loltrollbuild.test.batch.BatchTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@BatchTest(ItemsRetrievalJobConfig.class)
@Import(ItemsRetrievalJobTestConfig.class)
public abstract class ItemsRetrievalJobConfigTest extends AbstractBatchTests {

	@MockBean
	private ItemsService itemsService;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	protected ImageFetcher imageFetcher;

	@Autowired
	private ItemsRetrievalJobLauncherTestUtils jobLauncherTestUtils;

	protected ItemsResponse itemsResponse;

	@Test
	public void savesNewItems() throws Exception {
		given(itemsService.getItems(eq(latestVersion)))
				.willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(itemsResponse.getItems().values().size()))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(itemsResponse.getItems().values());
	}

	@Test
	public void savesItemsDifference() throws Exception {
		given(itemsService.getItems(eq(latestVersion)))
				.willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		List<Item> items = itemsRepository.saveAll(itemsResponse.getItems().values());

		int index = (int) (Math.random() * (items.size() - 1)) + 1;
		Optional<Item> itemToEdit = itemsRepository.findById(items.get(index).getId());
		if (itemToEdit.isEmpty()) {
			fail("Unable to get a random Item to edit");
		}
		itemToEdit.get().setGroup("New Group");
		itemsRepository.save(itemToEdit.get());
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(itemsResponse.getItems().values());
	}

	@Test
	public void deletesItemsDifference() throws Exception {
		List<Item> items = itemsRepository.saveAll(itemsResponse.getItems().values());

		Item itemToDelete = RandomizeUtil.getRandom(items);
		itemsResponse.getItems().remove(String.valueOf(itemToDelete.getId()));

		given(itemsService.getItems(eq(latestVersion)))
				.willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(itemsResponse.getItems().values());
	}

	@Test
	public void noItemsFromRiotIsNoop() throws Exception {
		List<Item> items = itemsRepository.saveAll(itemsResponse.getItems().values());

		given(itemsService.getItems(eq(latestVersion)))
				.willReturn(new ArrayList<>());

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(items);
	}

}
