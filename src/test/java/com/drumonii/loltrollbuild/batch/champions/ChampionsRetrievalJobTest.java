package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ChampionsRetrievalJobTest extends BaseSpringTestRunner {

	private static final int MAX_RESPONSE_SIZE = 10;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	@Qualifier("championsRetrievalJob")
	private Job championsRetrievalJob;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;

	private ChampionsResponse championsResponseSlice;
	private String championsResponseBody;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);

		// Create a random "slice" of ChampionsResponse with size of MAX_RESPONSE_SIZE
		championsResponseSlice = new ChampionsResponse();
		championsResponseSlice.setType(championsResponse.getType());
		championsResponseSlice.setVersion(championsResponse.getVersion());
		championsResponseSlice.setChampions(RandomizeUtil.getRandoms(
				championsResponse.getChampions().values(), MAX_RESPONSE_SIZE).stream()
				.collect(Collectors.toMap(champion -> String.valueOf(champion.getId()), champion -> champion,
						(key, value) -> key, LinkedHashMap::new)));

		try {
			championsResponseBody = objectMapper.writeValueAsString(championsResponseSlice);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Champions response.", e);
		}

		versionsRepository.save(versions.get(0));

		jobLauncherTestUtils.setJob(championsRetrievalJob);
	}

	@After
	public void after() {
		championsRepository.deleteAll();
	}

	@Test
	public void savesNewChampions() throws Exception {
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobLauncherTestUtils.getUniqueJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(championsResponseSlice.getChampions().values());
	}

	@Test
	public void savesChampionsDifference() throws Exception {
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		List<Champion> champions = championsRepository.save(championsResponseSlice.getChampions().values());

		Champion championToEdit = RandomizeUtil.getRandom(champions);
		championToEdit.setTags(new TreeSet<>(Arrays.asList("NEW_TAG")));

		championsRepository.save(championToEdit);

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobLauncherTestUtils.getUniqueJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(championsResponseSlice.getChampions().values());
	}

	@Test
	public void deletesChampionsDifference() throws Exception {
		List<Champion> champions = championsRepository.save(championsResponseSlice.getChampions().values());

		Champion championToDelete = RandomizeUtil.getRandom(champions);
		championsResponseSlice.getChampions().remove(String.valueOf(championToDelete.getId()));

		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(championsResponseSlice),
						MediaType.APPLICATION_JSON_UTF8));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobLauncherTestUtils.getUniqueJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(championsResponseSlice.getChampions().values());
	}

}