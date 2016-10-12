package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class VersionsRetrievalJobTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	@Qualifier("versionsRetrievalJob")
	private Job versionsRetrievalJob;

	@Autowired
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);

		String versionsResponseBody = null;
		try {
			versionsResponseBody = objectMapper.writeValueAsString(versions);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Versions.", e);
		}

		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		jobLauncherTestUtils.setJob(versionsRetrievalJob);
	}

	@After
	public void after() {
		versionsRepository.deleteAll();
	}

	@Test
	public void savesNewVersions() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(versionsRepository.findAll(new Sort(DESC, "major", "minor", "revision")))
				.containsExactlyElementsOf(versions);
	}

	@Test
	public void ignoresOldVersions() throws Exception {
		versionsRepository.save(versions);

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(versionsRepository.findAll(new Sort(DESC, "major", "minor", "revision")))
				.containsExactlyElementsOf(versions);
	}

}