package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.test.rest.WebMvcRestTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneId;
import java.util.Optional;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest(ImagesRestController.class)
public abstract class ImagesRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	protected ChampionsResponse championsResponse;
	protected ItemsResponse itemsResponse;
	protected MapsResponse mapsResponse;
	protected SummonerSpellsResponse summonerSpellsResponse;

	public abstract void before();

	@Test
	public void summonerSpellImg() throws Exception {
		mockMvc.perform(get("/img/summoner-spells/{img}.{fileExt}", 0,  "jpg"))
				.andExpect(status().isNotFound());

		SummonerSpell smite = summonerSpellsResponse.getSummonerSpells().get("SummonerSmite");
		smite = summonerSpellsRepository.save(smite);

		String fileExt = FilenameUtils.getExtension(smite.getImage().getFull());

		mockMvc.perform(get("/img/summoner-spells/{img}.{fileExt}", smite.getId(),  fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", smite.getLastModifiedDate()
						.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
	}

	@Test
	public void itemImg() throws Exception {
		mockMvc.perform(get("/img/items/{img}.{fileExt}", 0, "jpg"))
				.andExpect(status().isNotFound());

		Item thornmail = itemsResponse.getItems().get("3075");
		thornmail = itemsRepository.save(thornmail);

		String fileExt = FilenameUtils.getExtension(thornmail.getImage().getFull());

		mockMvc.perform(get("/img/items/{img}.{fileExt}", thornmail.getId(), fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", thornmail.getLastModifiedDate()
						.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
	}

	@Test
	public void championImg() throws Exception {
		mockMvc.perform(get("/img/champions/{img}.{fileExt}", 0, "jpg"))
				.andExpect(status().isNotFound());

		Champion shen = championsResponse.getChampions().get("Shen");
		shen = championsRepository.save(shen);

		String fileExt = FilenameUtils.getExtension(shen.getImage().getFull());

		mockMvc.perform(get("/img/champions/{img}.{fileExt}", shen.getId(), fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", shen.getLastModifiedDate()
						.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
	}

	@Test
	public void championSpellImg() throws Exception {
		mockMvc.perform(get("/img/champions/{id}/spell/{img}.{fileExt}", 0, "key", "jpg"))
				.andExpect(status().isNotFound());

		Champion azir = championsResponse.getChampions().get("Azir");
		azir = championsRepository.save(azir);
		Optional<ChampionSpell> spell = azir.getSpells().stream().findAny();
		if (!spell.isPresent()) {
			fail("Unable to get a Champion Spell from the Champion");
		}
		String fileExt = FilenameUtils.getExtension(spell.get().getImage().getFull());

		mockMvc.perform(get("/img/champions/{id}/spell/{img}.{fileExt}", azir.getId(), spell.get().getKey(), fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", azir.getLastModifiedDate()
						.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));

		mockMvc.perform(get("/img/champions/{id}/spell/{img}", azir.getId(), "test." + fileExt))
				.andExpect(status().isNotFound());
	}

	@Test
	public void championPassiveImg() throws Exception {
		mockMvc.perform(get("/img//champions/{id}/passive/{passive}.*", 0, "image.jpg"))
				.andExpect(status().isNotFound());

		Champion ekko = championsResponse.getChampions().get("Ekko");
		ekko = championsRepository.save(ekko);

		String fileExt = FilenameUtils.getExtension(ekko.getPassive().getImage().getFull());

		mockMvc.perform(get("/img//champions/{id}/passive/{passive}.*", ekko.getId(),
				ekko.getPassive().getImage().getFull() + "." + fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", ekko.getLastModifiedDate()
						.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
	}

	@Test
	public void mapImg() throws Exception {
		mockMvc.perform(get("/img/maps/map{img}.{fileExt}", 0, "jpg"))
				.andExpect(status().isNotFound());

		GameMap summonersRift = mapsResponse.getMaps().get(SUMMONERS_RIFT_SID);
		summonersRift = mapsRepository.save(summonersRift);

		String fileExt = FilenameUtils.getExtension(summonersRift.getImage().getFull());

		mockMvc.perform(get("/img/maps/map{img}.{fileExt}", summonersRift.getMapId(), fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", summonersRift.getLastModifiedDate()
						.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
	}

}