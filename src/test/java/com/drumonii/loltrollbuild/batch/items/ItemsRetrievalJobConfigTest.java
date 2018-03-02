package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.service.ItemsService;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(ItemsRetrievalJobConfigTestConfiguration.class)
public abstract class ItemsRetrievalJobConfigTest {

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

	protected Version latestVersion;

	public abstract void before();

	@After
	public void after() {
		itemsRepository.deleteAll();
	}

	@Test
	public void savesNewItems() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(itemsResponse.getItems().values().size()))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(itemsResponse.getItems().values());
	}

	@Test
	public void savesItemsDifference() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		List<Item> items = itemsRepository.saveAll(itemsResponse.getItems().values());

		Optional<Item> itemToEdit = itemsRepository.findById(items.get(RandomUtils.nextInt(1, items.size())).getId());
		if (!itemToEdit.isPresent()) {
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

		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(itemsResponse.getItems().values());
	}

	private JobParameters getJobParameters() {
		return new JobParametersBuilder()
				.addString("latestRiotPatch", latestVersion.getPatch())
				.addDouble("random", Math.random())
				.toJobParameters();
	}

}