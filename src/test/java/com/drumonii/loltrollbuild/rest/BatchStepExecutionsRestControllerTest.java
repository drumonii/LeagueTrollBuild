package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.riot.service.*;
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
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureCache
@Transactional
@ActiveProfiles({ TESTING })
public class BatchStepExecutionsRestControllerTest {

	private static final MediaType HAL_JSON_UTF8 = new MediaType("application", "hal+json", UTF_8);

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

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	private JobInstance jobInstance;
	private StepExecution stepExecution;

	@Before
	public void before() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("someJobParameter", RandomStringUtils.randomAlphabetic(15))
				.toJobParameters();

		jobInstance = jobInstanceDao.createJobInstance(RandomStringUtils.randomAlphabetic(7), jobParameters);

		jobExecutionDao.saveJobExecution(new JobExecution(jobInstance, jobParameters, RandomStringUtils.randomAlphabetic(5)));
		JobExecution jobExecution = jobExecutionDao.getLastJobExecution(jobInstance);

		stepExecutionDao.saveStepExecution(new StepExecution(RandomStringUtils.randomAlphabetic(8), jobExecution));
		List<StepExecution> stepExecutions =
				jdbcTemplate.query("SELECT * FROM BATCH_STEP_EXECUTION WHERE JOB_EXECUTION_ID = ? ORDER BY STEP_EXECUTION_ID",
						new StepExecutionRowMapper(jobExecution), jobExecution.getId());
		if (stepExecutions.isEmpty()) {
			fail("Unable to find recent StepExecution for JobExecution Id: " + jobExecution.getId());
		}
		stepExecution = stepExecutions.get(0);
	}

	@WithMockAdminUser
	@Test
	public void getBatchStepExecutions() throws Exception {
		mockMvc.perform(get(apiPath + "/job-instances/{jobInstanceId}/step-executions", jobInstance.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.stepExecutions", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists());

		mockMvc.perform(get(apiPath + "/job-instances/{jobInstanceId}/step-executions", -1))
				.andExpect(status().isNotFound());
	}

	@WithMockAdminUser
	@Test
	public void getBatchStepExecution() throws Exception {
		mockMvc.perform(get("{apiPath}/job-instances/{jobInstanceId}/step-executions/{stepExecutionId}", apiPath,
				jobInstance.getId(), stepExecution.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists());

		mockMvc.perform(get("{apiPath}/job-instances/{jobInstanceId}/step-executions/{stepExecutionId}", apiPath, -1, -1))
				.andExpect(status().isNotFound());
	}

	private class StepExecutionRowMapper implements RowMapper<StepExecution> {

		private JobExecution jobExecution;

		StepExecutionRowMapper(JobExecution jobExecution) {
			this.jobExecution = jobExecution;
		}

		@Override
		public StepExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
			StepExecution stepExecution = new StepExecution(rs.getString("STEP_NAME"), jobExecution, rs.getLong("STEP_EXECUTION_ID"));
			stepExecution.setStartTime(rs.getTimestamp("START_TIME"));
			stepExecution.setEndTime(rs.getTimestamp("END_TIME"));
			stepExecution.setStatus(BatchStatus.valueOf(rs.getString("STATUS")));
			stepExecution.setCommitCount(rs.getInt("COMMIT_COUNT"));
			stepExecution.setReadCount(rs.getInt("READ_COUNT"));
			stepExecution.setFilterCount(rs.getInt("FILTER_COUNT"));
			stepExecution.setWriteCount(rs.getInt("WRITE_COUNT"));
			stepExecution.setExitStatus(new ExitStatus(rs.getString("EXIT_CODE"), rs.getString("EXIT_MESSAGE")));
			stepExecution.setReadSkipCount(rs.getInt("READ_SKIP_COUNT"));
			stepExecution.setWriteSkipCount(rs.getInt("WRITE_SKIP_COUNT"));
			stepExecution.setProcessSkipCount(rs.getInt("PROCESS_SKIP_COUNT"));
			stepExecution.setRollbackCount(rs.getInt("ROLLBACK_COUNT"));
			stepExecution.setLastUpdated(rs.getTimestamp("LAST_UPDATED"));
			stepExecution.setVersion(rs.getInt("VERSION"));
			return stepExecution;
		}

	}

	@TestConfiguration
	static class BatchStepExecutionsRestControllerTestConfiguration {

		@Bean
		public DataFieldMaxValueIncrementerFactory dataFieldMaxValueIncrementerFactory(DataSource dataSource) {
			return new DefaultDataFieldMaxValueIncrementerFactory(dataSource);
		}

		@Bean
		public JobInstanceDao jobInstanceDao(JdbcTemplate jdbcTemplate, DataSource dataSource,
				DataFieldMaxValueIncrementerFactory dataFieldMaxValueIncrementerFactory) throws
				MetaDataAccessException {
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