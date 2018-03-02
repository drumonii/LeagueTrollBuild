package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.riot.service.SummonerSpellsService;
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
@Import(SummonerSpellsRetrievalJobConfigTestConfiguration.class)
public abstract class SummonerSpellsRetrievalJobConfigTest {

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

	protected Version latestVersion;

	public abstract void before();

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
	}

	@Test
	public void savesNewSummonerSpells() throws Exception {
		given(summonerSpellsService.getSummonerSpells())
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(summonerSpellsResponse.getSummonerSpells().values().size()))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponse
				.getSummonerSpells().values());
	}

	@Test
	public void savesSummonerSpellsDifference() throws Exception {
		given(summonerSpellsService.getSummonerSpells())
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		List<SummonerSpell> summonerSpells = summonerSpellsRepository.saveAll(summonerSpellsResponse
				.getSummonerSpells().values());

		Optional<SummonerSpell> summonerSpellToEdit =
				summonerSpellsRepository.findById(summonerSpells.get(RandomUtils.nextInt(1, summonerSpells.size())).getId());
		if (!summonerSpellToEdit.isPresent()) {
			fail("Unable to get a random Summoner Spell to edit");
		}
		summonerSpellToEdit.get().setDescription("New Description");
		summonerSpellsRepository.save(summonerSpellToEdit.get());

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponse
				.getSummonerSpells().values());
	}

	@Test
	public void deletesSummonerSpellsDifference() throws Exception {
		List<SummonerSpell> summonerSpells = summonerSpellsRepository.saveAll(summonerSpellsResponse
				.getSummonerSpells().values());

		SummonerSpell summonerSpellToDelete = RandomizeUtil.getRandom(summonerSpells);
		summonerSpellsResponse.getSummonerSpells().remove(summonerSpellToDelete.getKey());

		given(summonerSpellsService.getSummonerSpells())
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponse
				.getSummonerSpells().values());
	}

	private JobParameters getJobParameters() {
		return new JobParametersBuilder()
				.addString("latestRiotPatch", latestVersion.getPatch())
				.addDouble("random", Math.random())
				.toJobParameters();
	}

}