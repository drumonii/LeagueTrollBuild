package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.service.SummonerSpellsService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SummonerSpellsRetrievalJobConfigTest extends BaseSpringTestRunner {

	@MockBean
	private SummonerSpellsService summonerSpellsService;

	@Autowired
	@Qualifier("summonerSpellsRetrievalJob")
	private Job summonerSpellsRetrievalJob;

	@Before
	public void before() {
		super.before();

		jobLauncherTestUtils.setJob(summonerSpellsRetrievalJob);
	}

	@Test
	public void savesNewSummonerSpells() throws Exception {
		given(summonerSpellsService.getSummonerSpells())
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(summonerSpellsResponse.getSummonerSpells().values().size()))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponse
				.getSummonerSpells().values());
	}

	@Test
	public void savesSummonerSpellsDifference() throws Exception {
		given(summonerSpellsService.getSummonerSpells())
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

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

		given(summonerSpellsService.getSummonerSpells())
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponse
				.getSummonerSpells().values());
	}

}