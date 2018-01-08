package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.service.MapsService;
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

public class MapsRetrievalJobConfigTest extends BaseSpringTestRunner {

	@MockBean
	private MapsService mapsService;

	@Autowired
	@Qualifier("mapsRetrievalJob")
	private Job mapsRetrievalJob;

	@Before
	public void before() {
		super.before();

		jobLauncherTestUtils.setJob(mapsRetrievalJob);
	}

	@Test
	public void savesNewMaps() throws Exception {
		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(mapsResponse.getMaps().values().size()))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

	@Test
	public void savesMapsDifference() throws Exception {
		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		List<GameMap> maps = mapsRepository.save(mapsResponse.getMaps().values());

		GameMap mapToEdit = RandomizeUtil.getRandom(maps);
		mapToEdit.setMapName("New Map Name");

		mapsRepository.save(mapToEdit);
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

	@Test
	public void deletesMapsDifference() throws Exception {
		List<GameMap> maps = mapsRepository.save(mapsResponse.getMaps().values());

		GameMap mapToDelete = RandomizeUtil.getRandom(maps);
		mapsResponse.getMaps().remove(String.valueOf(mapToDelete.getMapId()));

		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, never())
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

}