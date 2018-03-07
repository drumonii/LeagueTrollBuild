package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.rest.processor.BatchJobInstanceProjectionResourceProcessor;
import com.drumonii.loltrollbuild.riot.service.*;
import com.drumonii.loltrollbuild.test.rest.WebMvcRestTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.dao.*;
import org.springframework.batch.item.database.support.DataFieldMaxValueIncrementerFactory;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.hateoas.MediaTypes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.rest.BatchJobInstancesRestController.PAGE_SIZE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest(controllers = BatchJobInstancesRestController.class,
		includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BatchJobInstanceProjectionResourceProcessor.class))
@ActiveProfiles({ TESTING })
public class BatchJobInstancesRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ChampionsService championsService;

	@MockBean
	private ItemsService itemsService;

	@MockBean
	private SummonerSpellsService summonerSpellsService;

	@MockBean
	private MapsService mapsService;

	@MockBean
	private VersionsService versionsService;

	@Autowired
	private JobInstanceDao jobInstanceDao;

	@Autowired
	private JobExecutionDao jobExecutionDao;

	@Autowired
	private StepExecutionDao stepExecutionDao;

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	private JobInstance jobInstance;

	@Before
	public void before() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("someJobParameter", RandomStringUtils.randomAlphabetic(15))
				.toJobParameters();

		jobInstance = jobInstanceDao.createJobInstance(RandomStringUtils.randomAlphabetic(7), jobParameters);

		jobExecutionDao.saveJobExecution(new JobExecution(jobInstance, jobParameters, RandomStringUtils.randomAlphabetic(5)));
		JobExecution jobExecution = jobExecutionDao.getLastJobExecution(jobInstance);

		stepExecutionDao.saveStepExecution(new StepExecution(RandomStringUtils.randomAlphabetic(8), jobExecution));
	}

	@WithMockAdminUser
	@Test
	public void getBatchJobInstances() throws Exception {
		// qbe
		mockMvc.perform(get("{apiPath}/job-instances", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.jobInstances").exists())
				.andExpect(jsonPath("$._embedded.jobInstances[*].jobExecution").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());

		// qbe with name
		mockMvc.perform(get("{apiPath}/job-instances", apiPath)
				.param("name", jobInstance.getJobName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.jobInstances", hasSize(1)))
				.andExpect(jsonPath("$._embedded.jobInstances[*].jobExecution").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());

		// qbe with no results
		mockMvc.perform(get("{apiPath}/job-instances", apiPath)
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements", is(0)))
				.andExpect(jsonPath("$.page.totalPages", is(0)))
				.andExpect(jsonPath("$.page.number", is(0)));
	}

	@WithMockAdminUser
	@Test
	public void getBatchJobInstance() throws Exception {
		mockMvc.perform(get("{apiPath}/job-instances/{jobInstanceId}", apiPath, jobInstance.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$.jobExecution").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists());

		mockMvc.perform(get("{apiPath}/job-instances/{jobInstanceId}", apiPath, -1))
				.andExpect(status().isNotFound());
	}

	@TestConfiguration
	static class BatchJobInstancesRestControllerTestConfiguration {

		@Bean
		public DataFieldMaxValueIncrementerFactory dataFieldMaxValueIncrementerFactory(DataSource dataSource) {
			return new DefaultDataFieldMaxValueIncrementerFactory(dataSource);
		}

		@Bean
		public JobInstanceDao jobInstanceDao(JdbcTemplate jdbcTemplate, DataSource dataSource,
				DataFieldMaxValueIncrementerFactory dataFieldMaxValueIncrementerFactory) throws MetaDataAccessException {
			JdbcJobInstanceDao jdbcJobInstanceDao = new JdbcJobInstanceDao();
			jdbcJobInstanceDao.setJdbcTemplate(jdbcTemplate);
			jdbcJobInstanceDao.setJobIncrementer(dataFieldMaxValueIncrementerFactory
					.getIncrementer(DatabaseType.fromMetaData(dataSource).name(), "BATCH_JOB_SEQ"));
			return jdbcJobInstanceDao;
		}

		@Bean
		public JobExecutionDao jobExecutionDao(JdbcTemplate jdbcTemplate, DataSource dataSource,
				DataFieldMaxValueIncrementerFactory dataFieldMaxValueIncrementerFactory) throws MetaDataAccessException {
			JdbcJobExecutionDao jdbcJobExecutionDao = new JdbcJobExecutionDao();
			jdbcJobExecutionDao.setJdbcTemplate(jdbcTemplate);
			jdbcJobExecutionDao.setJobExecutionIncrementer(dataFieldMaxValueIncrementerFactory
					.getIncrementer(DatabaseType.fromMetaData(dataSource).name(), "BATCH_JOB_EXECUTION_SEQ"));
			return jdbcJobExecutionDao;
		}

		@Bean
		public StepExecutionDao stepExecutionDao(JdbcTemplate jdbcTemplate, DataSource dataSource,
				DataFieldMaxValueIncrementerFactory dataFieldMaxValueIncrementerFactory) throws MetaDataAccessException {
			JdbcStepExecutionDao jdbcStepExecutionDao = new JdbcStepExecutionDao();
			jdbcStepExecutionDao.setJdbcTemplate(jdbcTemplate);
			jdbcStepExecutionDao.setStepExecutionIncrementer(dataFieldMaxValueIncrementerFactory
					.getIncrementer(DatabaseType.fromMetaData(dataSource).name(), "BATCH_STEP_EXECUTION_SEQ"));
			return jdbcStepExecutionDao;
		}

	}

}