package com.drumonii.loltrollbuild;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.*;
import com.drumonii.loltrollbuild.riot.api.*;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.ADMIN_ROLE;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_PASSWORD;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_USERNAME;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Base {@link SpringRunner} for Spring JUnit 4 tests that runs on a random unused port to avoid conflicts from the
 * main application (potentially) already running on port 8080.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = { "riot.api.key=API_KEY", "spring.cache.type=simple" })
@ActiveProfiles(TESTING)
public abstract class BaseSpringTestRunner {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	protected ChampionsRepository championsRepository;

	@Autowired
	protected ItemsRepository itemsRepository;

	@Autowired
	protected MapsRepository mapsRepository;

	@Autowired
	protected SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	protected VersionsRepository versionsRepository;

	@Autowired
	protected BuildsRepository buildsRepository;

	@Autowired
	protected BatchJobInstancesRepository batchJobInstancesRepository;

	@Autowired
	protected BatchJobExecutionsRepository batchJobExecutionsRepository;

	@Autowired
	protected BatchStepExecutionsRepository batchStepExecutionsRepository;

	@MockBean
	protected ImageFetcher imageFetcher;

	protected MockMvc mockMvc;

	protected static final MediaType HAL_JSON_UTF8 = new MediaType("application", "hal+json", UTF_8);

	@Value("${spring.data.rest.base-path}")
	protected String apiPath;

	protected static final int DEFAULT_PAGE_SIZE = 20;

	protected static final String TESTING_USERNAME = IN_MEM_USERNAME;
	protected static final String TESTING_PASSWORD = IN_MEM_PASSWORD;
	protected static final String TESTING_USER_ROLE = ADMIN_ROLE;

	protected static final String CRYSTAL_SCAR = String.valueOf(GameMapUtil.CRYSTAL_SCAR_ID);
	protected static final String TWISTED_TREELINE = String.valueOf(GameMapUtil.TWISTED_TREELINE_ID);
	protected static final String SUMMONERS_RIFT = String.valueOf(GameMapUtil.SUMMONERS_RIFT_ID);
	protected static final String HOWLING_ABYSS = String.valueOf(GameMapUtil.HOWLING_ABYSS_ID);

	protected ChampionsResponse championsResponse;
	protected ItemsResponse itemsResponse;
	protected MapsResponse mapsResponse;
	protected SummonerSpellsResponse summonerSpellsResponse;
	protected List<Version> versions;

	protected JobLauncherTestUtils jobLauncherTestUtils;

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

	@Before
	public void before() {
		mockMvc = webAppContextSetup(wac)
				.apply(documentationConfiguration(restDocumentation).uris()
						.withScheme("https")
						.withHost("loltrollbuild.com")
						.withPort(443))
				.apply(springSecurity())
				.build();

		// Build Champions from the JSON file
		ClassPathResource championsJson = new ClassPathResource("champions.json");
		try {
			championsResponse = objectMapper.readValue(championsJson.getFile(), ChampionsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Champions response.", e);
		}

		// Build Items from the JSON file
		ClassPathResource itemsJson = new ClassPathResource("items.json");
		try {
			itemsResponse = objectMapper.readValue(itemsJson.getFile(), ItemsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Items response.", e);
		}

		// Build Maps from the JSON file
		ClassPathResource mapsJson = new ClassPathResource("maps.json");
		try {
			mapsResponse = objectMapper.readValue(mapsJson.getFile(), MapsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
		}

		// Build Summoner Spells from the JSON file
		ClassPathResource summonerSpellsJson = new ClassPathResource("summoners.json");
		try {
			summonerSpellsResponse = objectMapper.readValue(summonerSpellsJson.getFile(), SummonerSpellsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Summoner Spells response.", e);
		}

		// Build Versions from the JSON file
		ClassPathResource versionsJson = new ClassPathResource("versions.json");
		try {
			versions = objectMapper.readValue(versionsJson.getFile(), new TypeReference<List<Version>>() {});
		} catch (IOException e) {
			fail("Unable to unmarshal the Versions response.", e);
		}

		jobLauncherTestUtils = new JobLauncherTestUtils();
		jobLauncherTestUtils.setJobLauncher(jobLauncher);
		jobLauncherTestUtils.setJobRepository(jobRepository);
	}

	@After
	public void after() {
		championsRepository.deleteAll();
		itemsRepository.deleteAll();
		mapsRepository.deleteAll();
		summonerSpellsRepository.deleteAll();
		versionsRepository.deleteAll();
		buildsRepository.deleteAll();
	}

	protected static RequestPostProcessor adminUser() {
		return user(TESTING_USERNAME).password(TESTING_PASSWORD).roles(TESTING_USER_ROLE);
	}

	protected JobParameters getJobParameters() {
		return new JobParametersBuilder()
				.addString("latestRiotPatch", versions.get(0).getPatch())
				.addLong("random", RandomUtils.nextLong(1, 1000000))
				.toJobParameters();
	}

}
