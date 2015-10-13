package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;

	@Before
	public void before() {
		super.before();
		mockServer = MockRestServiceServer.createServer(restTemplate);
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
		mockMvc.perform(get("/admin/summoner-spells").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("spells"))
				.andExpect(view().name("admin/summonerSpells/summonerSpells"));
	}

	@Test
	public void items() throws Exception {
		mockMvc.perform(get("/admin/items").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("items"))
				.andExpect(view().name("admin/items/items"));
	}

	@Test
	public void champions() throws Exception {
		mockMvc.perform(get("/admin/champions").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champions"))
				.andExpect(view().name("admin/champions/champions"));
	}

}