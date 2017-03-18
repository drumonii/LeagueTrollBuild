package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SummonerSpellsRetrievalJobConfigTest extends BaseSpringTestRunner {

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	@Qualifier("summonerSpellsRetrievalJob")
	private Job summonerSpellsRetrievalJob;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Before
	public void before() {
		super.before();

		jobLauncherTestUtils.setJob(summonerSpellsRetrievalJob);
	}

	@Test
	public void savesNewSummonerSpells() throws Exception {
		given(restTemplate.getForObject(eq(summonerSpellsUri.toString()), eq(SummonerSpellsResponse.class)))
				.willReturn(summonerSpellsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(summonerSpellsResponse.getSummonerSpells().values().size()))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponse
				.getSummonerSpells().values());
	}

	@Test
	public void savesSummonerSpellsDifference() throws Exception {
		given(restTemplate.getForObject(eq(summonerSpellsUri.toString()), eq(SummonerSpellsResponse.class)))
				.willReturn(summonerSpellsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		List<SummonerSpell> summonerSpells = summonerSpellsRepository.save(summonerSpellsResponse
				.getSummonerSpells().values());

		SummonerSpell summonerSpellToEdit = RandomizeUtil.getRandom(summonerSpells);
		summonerSpellToEdit.setDescription("New Description");

		summonerSpellsRepository.save(summonerSpellToEdit);
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponse
				.getSummonerSpells().values());
	}

	@Test
	public void deletesSummonerSpellsDifference() throws Exception {
		List<SummonerSpell> summonerSpells = summonerSpellsRepository.save(summonerSpellsResponse
				.getSummonerSpells().values());

		SummonerSpell summonerSpellToDelete = RandomizeUtil.getRandom(summonerSpells);
		summonerSpellsResponse.getSummonerSpells().remove(summonerSpellToDelete.getKey());

		given(restTemplate.getForObject(eq(summonerSpellsUri.toString()), eq(SummonerSpellsResponse.class)))
				.willReturn(summonerSpellsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(0))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponse
				.getSummonerSpells().values());
	}

}