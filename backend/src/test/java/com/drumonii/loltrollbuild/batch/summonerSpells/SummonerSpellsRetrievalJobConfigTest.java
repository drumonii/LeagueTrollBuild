package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.ImageFetcher;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.riot.service.SummonerSpellsService;
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
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@BatchTest(SummonerSpellsRetrievalJobConfig.class)
@Import(SummonerSpellsRetrievalJobTestConfig.class)
public abstract class SummonerSpellsRetrievalJobConfigTest extends AbstractBatchTests {

	@MockBean
	private SummonerSpellsService summonerSpellsService;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	protected ImageFetcher imageFetcher;

	@Autowired
	private SummonerSpellsRetrievalJobLauncherTestUtils jobLauncherTestUtils;

	protected SummonerSpellsResponse summonerSpellsResponse;

	@Test
	public void savesNewSummonerSpells() throws Exception {
		given(summonerSpellsService.getSummonerSpells(eq(latestVersion)))
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		List<SummonerSpell> summonerSpellsWithModes = summonerSpellsResponse.getSummonerSpells().values().stream()
				.filter(not(summonerSpell -> summonerSpell.getModes().isEmpty()))
				.collect(Collectors.toList());

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(summonerSpellsWithModes.size()))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsWithModes);
	}

	@Test
	public void savesSummonerSpellsDifference() throws Exception {
		given(summonerSpellsService.getSummonerSpells(eq(latestVersion)))
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		List<SummonerSpell> summonerSpellsWithModes = summonerSpellsResponse.getSummonerSpells().values().stream()
				.filter(not(summonerSpell -> summonerSpell.getModes().isEmpty()))
				.collect(Collectors.toList());

		List<SummonerSpell> summonerSpells = summonerSpellsRepository.saveAll(summonerSpellsWithModes);

		int index = (int) (Math.random() * (summonerSpells.size() - 1)) + 1;
		Optional<SummonerSpell> summonerSpellToEdit =
				summonerSpellsRepository.findById(summonerSpellsWithModes.get(index).getId());
		if (summonerSpellToEdit.isEmpty()) {
			fail("Unable to get a random Summoner Spell to edit");
		}
		summonerSpellToEdit.get().setDescription("New Description");
		summonerSpellsRepository.save(summonerSpellToEdit.get());

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsWithModes);
	}

	@Test
	public void deletesSummonerSpellsDifference() throws Exception {
		List<SummonerSpell> summonerSpells = summonerSpellsRepository.saveAll(summonerSpellsResponse
				.getSummonerSpells().values());

		SummonerSpell summonerSpellToDelete = RandomizeUtil.getRandom(summonerSpells);
		summonerSpellsResponse.getSummonerSpells().remove(summonerSpellToDelete.getKey());

		given(summonerSpellsService.getSummonerSpells(eq(latestVersion)))
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponse
				.getSummonerSpells().values());
	}

	@Test
	public void noSummonerSpellsFromRiotIsNoop() throws Exception {
		List<SummonerSpell> summonerSpells = summonerSpellsRepository.saveAll(summonerSpellsResponse
				.getSummonerSpells().values());

		given(summonerSpellsService.getSummonerSpells(eq(latestVersion)))
				.willReturn(new ArrayList<>());

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpells);
	}

}
