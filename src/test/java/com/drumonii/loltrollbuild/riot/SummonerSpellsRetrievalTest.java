package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.service.SummonerSpellsService;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SummonerSpellsRetrievalTest extends BaseSpringTestRunner {

	@MockBean
	private SummonerSpellsService summonerSpellsService;

	@MockBean
	private VersionsService versionsService;

	private SummonerSpell ignite;

	@Before
	public void before() {
		super.before();

		ignite = summonerSpellsResponse.getSummonerSpells().get("SummonerDot");
	}

	@Test
	public void summonerSpells() throws Exception {
		given(summonerSpellsService.getSummonerSpells())
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		mockMvc.perform(get("/riot/summoner-spells").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonerSpellsResponse
						.getSummonerSpells().values())));
	}

	@Test
	public void saveSummonerSpells() throws Exception {
		given(summonerSpellsService.getSummonerSpells())
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/summoner-spells").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonerSpellsResponse
						.getSummonerSpells().values())));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(summonerSpellsRepository.findAll())
				.containsOnlyElementsOf(summonerSpellsResponse.getSummonerSpells().values());
	}

	@Test
	public void saveDifferenceOfSummonerSpells() throws Exception {
		given(summonerSpellsService.getSummonerSpells())
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		List<SummonerSpell> summonerSpells = summonerSpellsRepository.save(summonerSpellsResponse
				.getSummonerSpells().values());

		mockMvc.perform(post("/riot/summoner-spells").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpells);
	}

	@Test
	public void saveDifferenceOfSummonerSpellsWithDeleted() throws Exception {
		List<SummonerSpell> summonerSpells = summonerSpellsRepository.save(summonerSpellsResponse
				.getSummonerSpells().values());
		SummonerSpell summonerSpellToDelete = RandomizeUtil.getRandom(summonerSpells);
		summonerSpellsResponse.getSummonerSpells().remove(summonerSpellToDelete.getKey());

		given(summonerSpellsService.getSummonerSpells())
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/summoner-spells").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(summonerSpellsRepository.findOne(summonerSpellToDelete.getId())).isNull();
	}

	@Test
	public void saveSummonerSpellsWithTruncate() throws Exception {
		given(summonerSpellsService.getSummonerSpells())
				.willReturn(new ArrayList<>(summonerSpellsResponse.getSummonerSpells().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/summoner-spells?truncate=true").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonerSpellsResponse
						.getSummonerSpells().values())));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(summonerSpellsRepository.findAll())
				.containsOnlyElementsOf(summonerSpellsResponse.getSummonerSpells().values());
	}

	@Test
	public void summonerSpell() throws Exception {
		given(summonerSpellsService.getSummonerSpell(eq(ignite.getId()))).willReturn(ignite);

		mockMvc.perform(get("/riot/summoner-spells/{id}", ignite.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(ignite)));
	}

	@Test
	public void summonerSpellNotFound() throws Exception {
		given(summonerSpellsService.getSummonerSpell(eq(ignite.getId()))).willReturn(null);

		mockMvc.perform(get("/riot/summoner-spells/{id}", ignite.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	public void saveSummonerSpell() throws Exception {
		given(summonerSpellsService.getSummonerSpell(eq(ignite.getId()))).willReturn(ignite);

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/summoner-spells/{id}", ignite.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(ignite)));

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(summonerSpellsRepository.findOne(ignite.getId())).isNotNull();
	}

	@Test
	public void saveSummonerSpellNotFound() throws Exception {
		given(summonerSpellsService.getSummonerSpell(eq(ignite.getId()))).willReturn(null);

		mockMvc.perform(post("/riot/summoner-spells/{id}", ignite.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	public void saveSummonerSpellWithOverwrite() throws Exception {
		SummonerSpell newIgnite = summonerSpellsResponse.getSummonerSpells().get("SummonerDot");
		newIgnite.setName("New Ignite");
		summonerSpellsRepository.save(newIgnite);

		given(summonerSpellsService.getSummonerSpell(eq(ignite.getId()))).willReturn(newIgnite);

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/summoner-spells/{id}", ignite.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(newIgnite)));

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(summonerSpellsRepository.findOne(newIgnite.getId())).isNotNull()
				.isEqualTo(newIgnite);
	}

}
