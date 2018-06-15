package com.drumonii.loltrollbuild.rest.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.config.WebSecurityConfig;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.service.ChampionsService;
import com.drumonii.loltrollbuild.riot.service.ItemsService;
import com.drumonii.loltrollbuild.riot.service.MapsService;
import com.drumonii.loltrollbuild.riot.service.SummonerSpellsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminRestController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles({ TESTING })
public class AdminRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

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

	@Value("${api.base-path}")
	private String apiPath;

	@WithMockAdminUser
	@Test
	public void summonerSpellsDifference() throws Exception {
		given(summonerSpellsService.getSummonerSpells()).willReturn(new ArrayList<>());
		given(summonerSpellsRepository.findAll()).willReturn(new ArrayList<>());

		mockMvc.perform(get("{apiPath}/admin/summoner-spells/diff", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

	@WithMockAdminUser
	@Test
	public void itemsDifference() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>());
		given(itemsRepository.findAll()).willReturn(new ArrayList<>());

		mockMvc.perform(get("{apiPath}/admin/items/diff", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

	@WithMockAdminUser
	@Test
	public void championsDifference() throws Exception {
		given(championsService.getChampions()).willReturn(new ArrayList<>());
		given(championsRepository.findAll()).willReturn(new ArrayList<>());

		mockMvc.perform(get("{apiPath}/admin/champions/diff", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

	@WithMockAdminUser
	@Test
	public void mapsDifference() throws Exception {
		given(mapsService.getMaps()).willReturn(new ArrayList<>());
		given(mapsRepository.findAll()).willReturn(new ArrayList<>());

		mockMvc.perform(get("{apiPath}/admin/maps/diff", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
	}

}