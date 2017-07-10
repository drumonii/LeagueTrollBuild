package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.service.ChampionsService;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChampionsRetrievalJobConfigTest extends BaseSpringTestRunner {

	@MockBean
	private ChampionsService championsService;

	@Autowired
	@Qualifier("championsRetrievalJob")
	private Job championsRetrievalJob;

	@Before
	public void before() {
		super.before();

		jobLauncherTestUtils.setJob(championsRetrievalJob);
	}

	@Test
	public void savesNewChampions() throws Exception {
		given(championsService.getChampions()).willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(championsResponse.getChampions().values().size() * 2))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));
		verify(imageFetcher, times(championsResponse.getChampions().values().size()))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findAll())
				.containsOnlyElementsOf(championsResponse.getChampions().values());
	}

	@Test
	public void savesChampionsDifference() throws Exception {
		given(championsService.getChampions()).willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		List<Champion> champions = championsRepository.save(championsResponse.getChampions().values());

		Champion championToEdit = RandomizeUtil.getRandom(champions);
		championToEdit.setTags(new TreeSet<>(Arrays.asList("NEW_TAG")));

		championsRepository.save(championToEdit);

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(2))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));
		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(championsResponse.getChampions().values());
	}

	@Test
	public void deletesChampionsDifference() throws Exception {
		List<Champion> champions = championsRepository.save(championsResponse.getChampions().values());

		Champion championToDelete = RandomizeUtil.getRandom(champions);
		championsResponse.getChampions().remove(championToDelete.getKey());

		given(championsService.getChampions()).willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(0))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));
		verify(imageFetcher, times(0))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findAll())
				.containsOnlyElementsOf(championsResponse.getChampions().values());
	}

}