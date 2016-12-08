package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminControllerTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

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
	@Qualifier("maps")
	private UriComponents mapsUri;

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
		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(versions), MediaType.APPLICATION_JSON_UTF8));

		versionsRepository.save(versions.get(0));

		mockMvc.perform(get("/admin").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("home")))
				.andExpect(view().name("admin/admin"));
	}

	@Test
	public void summonerSpells() throws Exception {
		mockServer.expect(times(2), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(versions), MediaType.APPLICATION_JSON_UTF8));
		mockServer.expect(times(2), requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(summonerSpellsResponse), MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/admin/summoner-spells").with(adminUser()))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("noSavedPatch", is("Summoner Spells")))
				.andExpect(redirectedUrl("/admin"));

		versionsRepository.save(versions.get(0));

		mockMvc.perform(get("/admin/summoner-spells").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("summonerSpells")))
				.andExpect(view().name("admin/summonerSpells/summonerSpells"));
	}

	@Test
	public void items() throws Exception {
		mockServer.expect(times(2), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(versions), MediaType.APPLICATION_JSON_UTF8));
		mockServer.expect(times(2), requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(itemsResponse), MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/admin/items").with(adminUser()))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("noSavedPatch", is("Items")))
				.andExpect(redirectedUrl("/admin"));

		versionsRepository.save(versions.get(0));

		mockMvc.perform(get("/admin/items").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("items")))
				.andExpect(view().name("admin/items/items"));
	}

	@Test
	public void champions() throws Exception {
		mockServer.expect(times(2), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(versions), MediaType.APPLICATION_JSON_UTF8));
		mockServer.expect(times(2), requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(championsResponse), MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/admin/champions").with(adminUser()).with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("noSavedPatch", is("Champions")))
				.andExpect(redirectedUrl("/admin"));

		versionsRepository.save(versions.get(0));

		mockMvc.perform(get("/admin/champions").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("champions")))
				.andExpect(view().name("admin/champions/champions"));
	}

	@Test
	public void maps() throws Exception {
		mockServer.expect(times(2), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(versions), MediaType.APPLICATION_JSON_UTF8));
		mockServer.expect(times(2), requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(mapsResponse), MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/admin/maps").with(adminUser()).with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("noSavedPatch", is("Maps")))
				.andExpect(redirectedUrl("/admin"));

		versionsRepository.save(versions.get(0));

		mockMvc.perform(get("/admin/maps").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("maps")))
				.andExpect(view().name("admin/maps/maps"));
	}

}