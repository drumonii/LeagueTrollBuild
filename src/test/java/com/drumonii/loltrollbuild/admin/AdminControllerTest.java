package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminControllerTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;

	@Before
	public void before() {
		super.before();
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@After
	public void after() {
		versionsRepository.deleteAll();
	}

	@Test
	public void admin() throws Exception {
		String responseBody = "[\"5.16.1\",\"5.15.1\",\"5.14.1\",\"5.13.1\",\"5.12.1\",\"5.11.1\",\"5.10.1\"," +
				"\"5.9.1\",\"5.8.1\",\"5.7.2\",\"5.7.1\",\"5.6.2\",\"5.6.1\",\"5.5.3\",\"5.5.2\",\"5.5.1\"," +
				"\"5.4.1\",\"5.3.1\",\"5.2.2\",\"5.2.1\",\"5.1.2\",\"5.1.1\"]";
		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

		String[] newVersions = objectMapper.readValue(responseBody, String[].class);
		Version latestVersion = new Version(newVersions[0]);
		versionsRepository.save(latestVersion);

		mockMvc.perform(get("/admin").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(view().name("admin/admin"));
	}

	@Test
	public void summonerSpells() throws Exception {
		versionsRepository.save(new Version("new version"));

		String responseBody = "{\"type\":\"summoner\",\"version\":\"5.16.1\",\"data\":{\"SummonerMana\":{\"name\":" +
				"\"Clarity\",\"description\":\"Restores 40% of your champion's maximum Mana. Also restores allies " +
				"for 40% of their maximum Mana\",\"image\":{\"full\":\"SummonerMana.png\",\"sprite\":\"spell0.png\"," +
				"\"group\":\"spell\",\"x\":384,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[180],\"summonerLevel\":1," +
				"\"id\":13,\"key\":\"SummonerMana\",\"modes\":[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\"," +
				"\"ASCENSION\"]}}}";
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

		mockMvc.perform(get("/admin/summoner-spells").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestSavedPatch", "difference"))
				.andExpect(view().name("admin/summonerSpells/summonerSpells"));
	}

	@Test
	public void items() throws Exception {
		versionsRepository.save(new Version("new version"));

		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3751\":{\"id\":3751,\"name\":" +
				"\"Bami's Cinder\",\"description\":\"<stats>+300 Health  </stats><br><br><unique>UNIQUE Passive - " +
				"Immolate:</unique> Deals 5 (+1 per champion level) magic damage per second to nearby enemies. Deals " +
				"50% bonus damage to minions and monsters.\",\"plaintext\":\"Grants Health and Immolate Aura\"," +
				"\"from\":[\"1028\"],\"into\":[\"3068\",\"3709\",\"3717\",\"3721\",\"3725\"],\"maps\":{\"1\":false," +
				"\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3751.png\"," +
				"\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":336,\"y\":288,\"w\":48,\"h\":48},\"gold\":" +
				"{\"base\":600,\"total\":1000,\"sell\":700,\"purchasable\":true}}}}";
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
		mockMvc.perform(get("/admin/items").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestSavedPatch", "difference"))
				.andExpect(view().name("admin/items/items"));
	}

	@Test
	public void champions() throws Exception {
		versionsRepository.save(new Version("new version"));

		String responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Nunu\":{\"id\":20,\"key\":" +
				"\"Nunu\",\"name\":\"Nunu\",\"title\":\"the Yeti Rider\",\"image\":{\"full\":\"Nunu.png\"," +
				"\"sprite\":\"champion2.png\",\"group\":\"champion\",\"x\":432,\"y\":0,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Support\",\"Fighter\"],\"partype\":\"Mana\"}}}}";
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

		mockMvc.perform(get("/admin/champions").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestSavedPatch", "difference"))
				.andExpect(view().name("admin/champions/champions"));
	}

}