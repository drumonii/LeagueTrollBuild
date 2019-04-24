package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.ImageFetcher;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.service.MapsService;
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
@BatchTest(MapsRetrievalJobConfig.class)
@Import(MapsRetrievalJobTestConfig.class)
public abstract class MapsRetrievalJobConfigTest extends AbstractBatchTests {

	@MockBean
	private MapsService mapsService;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	protected ImageFetcher imageFetcher;

	@Autowired
	private MapsRetrievalJobLauncherTestUtils jobLauncherTestUtils;

	protected MapsResponse mapsResponse;

	@Test
	public void savesNewMaps() throws Exception {
		given(mapsService.getMaps(eq(latestVersion)))
				.willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(mapsService, times(1)).getMaps(eq(latestVersion));

		verify(imageFetcher, times(mapsResponse.getMaps().values().size()))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

	@Test
	public void savesMapsDifference() throws Exception {
		given(mapsService.getMaps(eq(latestVersion)))
				.willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		List<GameMap> maps = mapsRepository.saveAll(mapsResponse.getMaps().values());

		int index = (int) (Math.random() * (maps.size() - 1)) + 1;
		Optional<GameMap> mapToEdit = mapsRepository.findById(maps.get(index).getMapId());
		if (mapToEdit.isEmpty()) {
			fail("Unable to get a random Map to edit");
		}
		mapToEdit.get().setMapName("New Map Name");
		mapsRepository.save(mapToEdit.get());
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(mapsService, times(1)).getMaps(eq(latestVersion));

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

	@Test
	public void deletesMapsDifference() throws Exception {
		List<GameMap> maps = mapsRepository.saveAll(mapsResponse.getMaps().values());

		GameMap mapToDelete = RandomizeUtil.getRandom(maps);
		mapsResponse.getMaps().remove(String.valueOf(mapToDelete.getMapId()));

		given(mapsService.getMaps(eq(latestVersion)))
				.willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(mapsService, times(1)).getMaps(eq(latestVersion));

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

	@Test
	public void noMapsFromRiotIsNoop() throws Exception {
		List<GameMap> maps = mapsRepository.saveAll(mapsResponse.getMaps().values());

		given(mapsService.getMaps(eq(latestVersion)))
				.willReturn(new ArrayList<>());

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.FAILED);

		verify(mapsService, times(6)).getMaps(eq(latestVersion));

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(maps);
	}

	@Test
	public void emptyMapsResponseRetries() throws Exception {
		given(mapsService.getMaps(eq(latestVersion)))
				.willReturn(new ArrayList<>());

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.FAILED);

		verify(mapsService, times(6)).getMaps(eq(latestVersion));

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(mapsRepository.findAll()).isEmpty();
	}

}
