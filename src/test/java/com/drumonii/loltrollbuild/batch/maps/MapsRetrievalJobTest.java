package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class MapsRetrievalJobTest extends BaseSpringTestRunner {

	private static final int MAX_RESPONSE_SIZE = 10;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Autowired
	@Qualifier("mapsRetrievalJob")
	private Job mapsRetrievalJob;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;

	private MapsResponse mapsResponseSlice;
	private String mapsResponseBody;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);

		// Create a random "slice" of MapsResponse with size of MAX_RESPONSE_SIZE
		mapsResponseSlice = new MapsResponse();
		mapsResponseSlice.setType(championsResponse.getType());
		mapsResponseSlice.setVersion(championsResponse.getVersion());
		mapsResponseSlice.setMaps(RandomizeUtil.getRandoms(
				mapsResponse.getMaps().values(), MAX_RESPONSE_SIZE).stream()
				.collect(Collectors.toMap(map -> String.valueOf(map.getMapId()), map -> map)));

		try {
			mapsResponseBody = objectMapper.writeValueAsString(mapsResponseSlice);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Maps response.", e);
		}

		versionsRepository.save(versions.get(0));

		jobLauncherTestUtils.setJob(mapsRetrievalJob);
	}

	@After
	public void after() {
		mapsRepository.deleteAll();
	}

	@Test
	public void savesNewMaps() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(mapsRepository.findAll()).containsOnlyElementsOf(mapsResponseSlice.getMaps().values());
	}

	@Test
	public void savesMapsDifference() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		List<GameMap> maps = mapsRepository.save(mapsResponseSlice.getMaps().values());

		GameMap mapToEdit = RandomizeUtil.getRandom(maps);
		mapToEdit.setMapName("New Map Name");

		mapsRepository.save(mapToEdit);
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(mapsRepository.findAll()).containsOnlyElementsOf(mapsResponseSlice.getMaps().values());
	}

	@Test
	public void deletesMapsDifference() throws Exception {
		List<GameMap> maps = mapsRepository.save(mapsResponseSlice.getMaps().values());

		GameMap mapToDelete = RandomizeUtil.getRandom(maps);
		mapsResponseSlice.getMaps().remove(String.valueOf(mapToDelete.getMapId()));

		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(mapsResponseSlice),
						MediaType.APPLICATION_JSON_UTF8));
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(mapsRepository.findAll()).containsOnlyElementsOf(mapsResponseSlice.getMaps().values());
	}

}