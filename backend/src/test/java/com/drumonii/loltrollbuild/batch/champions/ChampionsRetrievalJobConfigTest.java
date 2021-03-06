package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.ImageFetcher;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.service.ChampionsService;
import com.drumonii.loltrollbuild.test.batch.AbstractBatchTests;
import com.drumonii.loltrollbuild.test.batch.BatchTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@BatchTest(ChampionsRetrievalJobConfig.class)
abstract class ChampionsRetrievalJobConfigTest extends AbstractBatchTests {

	@MockBean
	private ChampionsService championsService;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	protected ImageFetcher imageFetcher;

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	protected ChampionsResponse championsResponse;

	@Test
	void savesNewChampions() throws Exception {
		given(championsService.getChampions(eq(latestVersion)))
				.willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(championsService, times(1)).getChampions(eq(latestVersion));

		verify(imageFetcher, times(championsResponse.getChampions().values().size() * 2))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));
		verify(imageFetcher, times(championsResponse.getChampions().values().size()))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findAll())
				.containsExactlyInAnyOrderElementsOf(championsResponse.getChampions().values());
	}

	@Test
	void savesChampionsDifference() throws Exception {
		given(championsService.getChampions(eq(latestVersion)))
				.willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		List<Champion> champions = championsRepository.saveAll(championsResponse.getChampions().values());

		int index = (int) (Math.random() * (champions.size() - 1)) + 1;
		Optional<Champion> championToEdit =
				championsRepository.findById(champions.get(index).getId());
		if (championToEdit.isEmpty()) {
			fail("Unable to get a random Champion to edit");
		}
		championToEdit.get().setTags(new TreeSet<>(Collections.singletonList("NEW_TAG")));
		championsRepository.save(championToEdit.get());

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(championsService, times(1)).getChampions(eq(latestVersion));

		verify(imageFetcher, times(2))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));
		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findAll()).containsExactlyInAnyOrderElementsOf(championsResponse.getChampions().values());
	}

	@Test
	void emptyChampionsResponseRetries() throws Exception {
		given(championsService.getChampions(eq(latestVersion)))
				.willReturn(new ArrayList<>());

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.FAILED);

		verify(championsService, times(6)).getChampions(eq(latestVersion));

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));
		verify(imageFetcher, never())
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findAll()).isEmpty();
	}

}
