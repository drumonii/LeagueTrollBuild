package com.drumonii.loltrollbuild;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.ADMIN_ROLE;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_PASSWORD;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_USERNAME;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Base {@link SpringJUnit4ClassRunner} for Spring JUnit 4 tests that runs on a random unused port to avoid conflicts
 * from the main application (potentially) already running on port 8080.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LeagueTrollBuildApplication.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles(TESTING)
@TestPropertySource(properties = "riot.api.key=API_KEY")
public abstract class BaseSpringTestRunner {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	protected ObjectMapper objectMapper;

	protected MockMvc mockMvc;

	protected static final String TESTING_USERNAME = IN_MEM_USERNAME;
	protected static final String TESTING_PASSWORD = IN_MEM_PASSWORD;
	protected static final String TESTING_USER_ROLE = ADMIN_ROLE;

	protected static final String CRYSTAL_SCAR = String.valueOf(GameMapUtil.CRYSTAL_SCAR_ID);
	protected static final String TWISTED_TREELINE = String.valueOf(GameMapUtil.TWISTED_TREELINE_ID);
	protected static final String SUMMONERS_RIFT = String.valueOf(GameMapUtil.SUMMONERS_RIFT_NEW_ID);
	protected static final String HOWLING_ABYSS = String.valueOf(GameMapUtil.HOWLING_ABYSS_ID);

	protected ChampionsResponse championsResponse;
	protected ItemsResponse itemsResponse;
	protected MapsResponse mapsResponse;
	protected SummonerSpellsResponse summonerSpellsResponse;
	protected List<Version> versions;

	@Rule
	public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

	@Before
	public void before() {
		mockMvc = webAppContextSetup(wac)
				.apply(documentationConfiguration(restDocumentation).uris()
						.withScheme("https")
						.withHost("league-troll-build.herokuapp.com")
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

		// Build Items from the JSON file
		ClassPathResource mapsJson = new ClassPathResource("maps.json");
		try {
			mapsResponse = objectMapper.readValue(mapsJson.getFile(), MapsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
		}

		// Build Items from the JSON file
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
	}

	protected static RequestPostProcessor adminUser() {
		return user(TESTING_USERNAME).password(TESTING_PASSWORD).roles(TESTING_USER_ROLE);
	}

}
