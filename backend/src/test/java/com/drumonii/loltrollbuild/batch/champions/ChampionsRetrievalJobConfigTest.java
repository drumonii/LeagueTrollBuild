package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.ImageFetcher;
import com.drumonii.loltrollbuild.riot.service.ChampionsService;
import com.drumonii.loltrollbuild.test.batch.BatchTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@BatchTest(ChampionsRetrievalJobConfig.class)
@Import(ChampionsRetrievalJobTestConfig.class)
public abstract class ChampionsRetrievalJobConfigTest {

	@MockBean
	private ChampionsService championsService;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	protected ImageFetcher imageFetcher;

	@Autowired
	private ChampionsRetrievalJobLauncherTestUtils jobLauncherTestUtils;

	protected ChampionsResponse championsResponse;

	protected Version latestVersion;

	public abstract void before();

	@Test
	public void savesNewChampions() throws Exception {
		given(championsService.getChampions(eq(latestVersion)))
				.willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(championsResponse.getChampions().values().size() * 2))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));
		verify(imageFetcher, times(championsResponse.getChampions().values().size()))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findAll())
				.containsOnlyElementsOf(championsResponse.getChampions().values());
	}

	@Test
	public void savesChampionsDifference() throws Exception {
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

		verify(imageFetcher, times(2))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));
		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(championsResponse.getChampions().values());
	}

	@Test
	public void deletesChampionsDifference() throws Exception {
		List<Champion> champions = championsRepository.saveAll(championsResponse.getChampions().values());

		Champion championToDelete = RandomizeUtil.getRandom(champions);
		championsResponse.getChampions().remove(championToDelete.getKey());

		given(championsService.getChampions(eq(latestVersion)))
				.willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));
		verify(imageFetcher, never())
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findAll())
				.containsOnlyElementsOf(championsResponse.getChampions().values());
	}

	private JobParameters getJobParameters() {
		return new JobParametersBuilder()
				.addString("latestRiotPatch", latestVersion.getPatch())
				.addDouble("random", Math.random())
				.toJobParameters();
	}

}