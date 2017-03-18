package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
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

public class MapsRetrievalJobConfigTest extends BaseSpringTestRunner {

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Autowired
	@Qualifier("mapsRetrievalJob")
	private Job mapsRetrievalJob;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Before
	public void before() {
		super.before();

		jobLauncherTestUtils.setJob(mapsRetrievalJob);
	}

	@Test
	public void savesNewMaps() throws Exception {
		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(mapsResponse.getMaps().values().size()))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

	@Test
	public void savesMapsDifference() throws Exception {
		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

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

		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(imageFetcher, times(0))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

}