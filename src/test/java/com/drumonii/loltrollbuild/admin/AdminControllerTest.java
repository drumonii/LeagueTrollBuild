package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.config.WebSecurityConfig;
import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.BatchStepExecution;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.*;
import com.drumonii.loltrollbuild.riot.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles({ TESTING })
public class AdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VersionsRepository versionsRepository;

	@MockBean
	private BatchJobInstancesRepository batchJobInstancesRepository;

	@MockBean
	private VersionsService versionsService;

	@MockBean
	private SummonerSpellsRepository summonerSpellsRepository;

	@MockBean
	private SummonerSpellsService summonerSpellsService;

	@MockBean
	private ItemsRepository itemsRepository;

	@MockBean
	private ItemsService itemsService;

	@MockBean
	private ChampionsRepository championsRepository;

	@MockBean
	private ChampionsService championsService;

	@MockBean
	private MapsRepository mapsRepository;

	@MockBean
	private MapsService mapsService;

	private Version latestVersion = new Version("7.17.2");

	@Before
	public void before() {
		given(versionsService.getLatestVersion()).willReturn(latestVersion);
	}

	@WithMockAdminUser
	@Test
	public void admin() throws Exception {
		given(versionsRepository.latestVersion()).willReturn(latestVersion);

		mockMvc.perform(get("/admin"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("home")))
				.andExpect(view().name("admin/admin"));

		given(versionsService.getLatestVersion()).willReturn(null);

		mockMvc.perform(get("/admin"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestSavedPatch"))
				.andExpect(model().attributeDoesNotExist("latestRiotPatch"))
				.andExpect(model().attribute("activeTab", is("home")))
				.andExpect(view().name("admin/admin"));
	}

	@WithMockAdminUser
	@Test
	public void summonerSpells() throws Exception {
		mockMvc.perform(get("/admin/summoner-spells"))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("noSavedPatch", is("Summoner Spells")))
				.andExpect(redirectedUrl("/admin"));

		given(versionsRepository.latestVersion()).willReturn(latestVersion);

		mockMvc.perform(get("/admin/summoner-spells"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("summonerSpells")))
				.andExpect(view().name("admin/summonerSpells/summonerSpells"));
	}

	@WithMockAdminUser
	@Test
	public void summonerSpellsDifference() throws Exception {
		given(summonerSpellsService.getSummonerSpells()).willReturn(new ArrayList<>());

		mockMvc.perform(get("/admin/summoner-spells/diff"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

	@WithMockAdminUser
	@Test
	public void items() throws Exception {
		mockMvc.perform(get("/admin/items"))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("noSavedPatch", is("Items")))
				.andExpect(redirectedUrl("/admin"));

		given(versionsRepository.latestVersion()).willReturn(latestVersion);

		mockMvc.perform(get("/admin/items"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("items")))
				.andExpect(view().name("admin/items/items"));
	}

	@WithMockAdminUser
	@Test
	public void itemsDifference() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>());

		mockMvc.perform(get("/admin/items/diff"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

	@WithMockAdminUser
	@Test
	public void champions() throws Exception {
		mockMvc.perform(get("/admin/champions").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("noSavedPatch", is("Champions")))
				.andExpect(redirectedUrl("/admin"));

		given(versionsRepository.latestVersion()).willReturn(latestVersion);

		mockMvc.perform(get("/admin/champions"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("champions")))
				.andExpect(view().name("admin/champions/champions"));
	}

	@WithMockAdminUser
	@Test
	public void championsDifference() throws Exception {
		given(championsService.getChampions()).willReturn(new ArrayList<>());

		mockMvc.perform(get("/admin/champions/diff"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

	@WithMockAdminUser
	@Test
	public void maps() throws Exception {
		mockMvc.perform(get("/admin/maps").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("noSavedPatch", is("Maps")))
				.andExpect(redirectedUrl("/admin"));

		given(versionsRepository.latestVersion()).willReturn(latestVersion);

		mockMvc.perform(get("/admin/maps"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("maps")))
				.andExpect(view().name("admin/maps/maps"));
	}

	@WithMockAdminUser
	@Test
	public void mapsDifference() throws Exception {
		given(mapsService.getMaps()).willReturn(new ArrayList<>());

		mockMvc.perform(get("/admin/maps/diff"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

	@WithMockAdminUser
	@Test
	public void jobInstances() throws Exception {
		mockMvc.perform(get("/admin/job-instances"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("activeTab", is("jobInstances")))
				.andExpect(view().name("admin/jobs/jobInstances"));
	}

	@WithMockAdminUser
	@Test
	public void stepExecutions() throws Exception {
		BatchJobInstance jobInstance = new BatchJobInstance();
		jobInstance.setId(1);
		jobInstance.setName("jobInstanceName");
		jobInstance.setKey("jobInstanceKey");

		BatchJobExecution jobExecution = new BatchJobExecution();
		jobExecution.setCreateTime(LocalDateTime.now());
		jobExecution.setJobInstance(jobInstance);
		jobInstance.setJobExecution(jobExecution);

		BatchStepExecution stepExecution = new BatchStepExecution();
		stepExecution.setVersion(1L);
		stepExecution.setName("step1");
		stepExecution.setStartTime(LocalDateTime.now());
		stepExecution.setJobExecution(jobExecution);
		jobExecution.setStepExecutions(new HashSet<>(Collections.singletonList(stepExecution)));

		given(batchJobInstancesRepository.findById(eq(jobInstance.getId()))).willReturn(Optional.of(jobInstance));

		mockMvc.perform(get("/admin/job-instances/{jobInstanceId}/step-executions", jobInstance.getId()))
				.andExpect(status().isOk())
				.andExpect(model().attribute("activeTab", is("jobInstances")))
				.andExpect(view().name("admin/jobs/stepExecutions"));
	}

}