package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChampionsRetrievalJobConfigTest extends BaseSpringTestRunner {

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	@Qualifier("championsRetrievalJob")
	private Job championsRetrievalJob;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Before
	public void before() {
		super.before();

		jobLauncherTestUtils.setJob(championsRetrievalJob);
	}

	@Test
	public void savesNewChampions() throws Exception {
		given(restTemplate.getForObject(eq(championsUri.toString()), eq(ChampionsResponse.class)))
				.willReturn(championsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

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
		given(restTemplate.getForObject(eq(championsUri.toString()), eq(ChampionsResponse.class)))
				.willReturn(championsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

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

		given(restTemplate.getForObject(eq(championsUri.toString()), eq(ChampionsResponse.class)))
				.willReturn(championsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

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