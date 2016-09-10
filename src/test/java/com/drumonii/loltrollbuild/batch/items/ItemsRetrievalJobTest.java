package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
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

public class ItemsRetrievalJobTest extends BaseSpringTestRunner {

	private static final int MAX_RESPONSE_SIZE = 10;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	@Qualifier("itemsRetrievalJob")
	private Job itemsRetrievalJob;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;

	private ItemsResponse itemsResponseSlice;
	private String itemsResponseBody;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);

		// Create a random "slice" of ItemsResponse with size of MAX_RESPONSE_SIZE
		itemsResponseSlice = new ItemsResponse();
		itemsResponseSlice.setType(itemsResponse.getType());
		itemsResponseSlice.setVersion(itemsResponse.getVersion());
		itemsResponseSlice.setItems(RandomizeUtil.getRandoms(
				itemsResponse.getItems().values(), MAX_RESPONSE_SIZE).stream()
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));

		try {
			itemsResponseBody = objectMapper.writeValueAsString(itemsResponseSlice);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Items response.", e);
		}

		versionsRepository.save(versions.get(0));

		jobLauncherTestUtils.setJob(itemsRetrievalJob);
	}

	@After
	public void after() {
		itemsRepository.deleteAll();
	}

	@Test
	public void savesNewItems() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(itemsRepository.findAll()).containsOnlyElementsOf(itemsResponseSlice.getItems().values());
	}

	@Test
	public void savesItemsDifference() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		List<Item> items = itemsRepository.save(itemsResponseSlice.getItems().values());

		Item itemToEdit = RandomizeUtil.getRandom(items);
		itemToEdit.setGroup("New Group");

		itemsRepository.save(itemToEdit);
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(itemsRepository.findAll()).containsOnlyElementsOf(itemsResponseSlice.getItems().values());
	}

	@Test
	public void deletesItemsDifference() throws Exception {
		List<Item> items = itemsRepository.save(itemsResponseSlice.getItems().values());

		Item itemToDelete = RandomizeUtil.getRandom(items);
		itemsResponseSlice.getItems().remove(String.valueOf(itemToDelete.getId()));

		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(itemsResponseSlice),
						MediaType.APPLICATION_JSON_UTF8));
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(itemsRepository.findAll()).containsOnlyElementsOf(itemsResponseSlice.getItems().values());
	}

}