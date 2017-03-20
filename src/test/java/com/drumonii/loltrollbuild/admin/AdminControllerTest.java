package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.BatchStepExecution;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.*;
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
import static org.mockito.Matchers.anyListOf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminControllerTest extends BaseSpringTestRunner {

	@MockBean
	private VersionsRetrieval versionsRetrieval;

	@MockBean
	private SummonerSpellsRetrieval summonerSpellsRetrieval;

	@MockBean
	private ItemsRetrieval itemsRetrieval;

	@MockBean
	private ChampionsRetrieval championsRetrieval;

	@MockBean
	private MapsRetrieval mapsRetrieval;

	@Before
	public void before() {
		super.before();

		given(versionsRetrieval.latestVersion(anyListOf(Version.class))).willReturn(versions.get(0));
	}

	@Test
	public void admin() throws Exception {
		versionsRepository.save(versions.get(0));

		mockMvc.perform(get("/admin").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestRiotPatch", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("home")))
				.andExpect(view().name("admin/admin"));
	}

	@Test
	public void summonerSpells() throws Exception {
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
	public void summonerSpellsDifference() throws Exception {
		given(summonerSpellsRetrieval.summonerSpells()).willReturn(new ArrayList<>());

		mockMvc.perform(get("/admin/summoner-spells/diff").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

	@Test
	public void items() throws Exception {
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
	public void itemsDifference() throws Exception {
		given(itemsRetrieval.items()).willReturn(new ArrayList<>());

		mockMvc.perform(get("/admin/items/diff").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

	@Test
	public void champions() throws Exception {
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
	public void championsDifference() throws Exception {
		given(championsRetrieval.champions()).willReturn(new ArrayList<>());

		mockMvc.perform(get("/admin/champions/diff").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

	@Test
	public void maps() throws Exception {
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

	@Test
	public void mapsDifference() throws Exception {
		given(mapsRetrieval.maps()).willReturn(new ArrayList<>());

		mockMvc.perform(get("/admin/maps/diff").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

	@Test
	public void jobInstances() throws Exception {
		mockMvc.perform(get("/admin/job-instances").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attribute("activeTab", is("jobInstances")))
				.andExpect(view().name("admin/jobs/jobInstances"));
	}

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

		mockMvc.perform(get("/admin/job-instances/{jobInstanceId}/step-executions", jobInstance.getId()).with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attribute("activeTab", is("jobInstances")))
				.andExpect(view().name("admin/jobs/stepExecutions"));
	}

}