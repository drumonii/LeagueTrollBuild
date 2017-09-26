package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.BatchStepExecution;
import com.drumonii.loltrollbuild.riot.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminControllerTest extends BaseSpringTestRunner {

	@MockBean
	private VersionsService versionsService;

	@MockBean
	private SummonerSpellsService summonerSpellsService;

	@MockBean
	private ItemsService itemsService;

	@MockBean
	private ChampionsService championsService;

	@MockBean
	private MapsService mapsService;

	@Before
	public void before() {
		super.before();

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));
	}

	@WithMockAdminUser
	@Test
	public void admin() throws Exception {
		versionsRepository.save(versions.get(0));

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

		versionsRepository.save(versions.get(0));

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

		versionsRepository.save(versions.get(0));

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

		versionsRepository.save(versions.get(0));

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

		versionsRepository.save(versions.get(0));

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
		jobInstance.setName(RandomStringUtils.randomAlphabetic(7));
		jobInstance.setKey(RandomStringUtils.randomAlphabetic(10));
		jobInstance = batchJobInstancesRepository.save(jobInstance);

		BatchJobExecution jobExecution = new BatchJobExecution();
		jobExecution.setCreateTime(LocalDateTime.now());
		jobExecution.setJobInstance(jobInstance);
		jobExecution = batchJobExecutionsRepository.save(jobExecution);

		BatchStepExecution stepExecution = new BatchStepExecution();
		stepExecution.setVersion(RandomUtils.nextLong());
		stepExecution.setName(RandomStringUtils.randomAlphabetic(10));
		stepExecution.setStartTime(LocalDateTime.now());
		stepExecution.setJobExecution(jobExecution);
		batchStepExecutionsRepository.save(stepExecution);

		mockMvc.perform(get("/admin/job-instances/{jobInstanceId}/step-executions", jobInstance.getId()))
				.andExpect(status().isOk())
				.andExpect(model().attribute("activeTab", is("jobInstances")))
				.andExpect(view().name("admin/jobs/stepExecutions"));
	}

}