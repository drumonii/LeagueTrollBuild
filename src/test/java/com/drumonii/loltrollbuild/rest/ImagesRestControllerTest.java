package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImagesRestControllerTest extends BaseSpringTestRunner {

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private MapsRepository mapsRepository;

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
		itemsRepository.deleteAll();
		championsRepository.deleteAll();
		mapsRepository.deleteAll();
	}

	@Test
	public void summonerSpellImg() throws Exception {
		SummonerSpell smite = summonerSpellsResponse.getSummonerSpells().get("SummonerSmite");
		smite = summonerSpellsRepository.save(smite);

		String fileExt = FilenameUtils.getExtension(smite.getImage().getFull());

		mockMvc.perform(get("/img/summoner-spells/{img}", smite.getId() + "." + fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", smite.getLastModifiedDate().getTime()));
	}

	@Test
	public void itemImg() throws Exception {
		Item thornmail = itemsResponse.getItems().get("3075");
		thornmail = itemsRepository.save(thornmail);

		String fileExt = FilenameUtils.getExtension(thornmail.getImage().getFull());

		mockMvc.perform(get("/img/items/{img}", thornmail.getId() + "." + fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", thornmail.getLastModifiedDate().getTime()));
	}

	@Test
	public void championImg() throws Exception {
		Champion shen = championsResponse.getChampions().get("Shen");
		shen = championsRepository.save(shen);

		String fileExt = FilenameUtils.getExtension(shen.getImage().getFull());

		mockMvc.perform(get("/img/champions/{img}", shen.getId() + "." + fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", shen.getLastModifiedDate().getTime()));
	}

	@Test
	public void championSpellImg() throws Exception {
		Champion azir = championsResponse.getChampions().get("Azir");
		azir = championsRepository.save(azir);
		ChampionSpell spell = RandomizeUtil.getRandom(azir.getSpells());

		String fileExt = FilenameUtils.getExtension(spell.getImage().getFull());

		mockMvc.perform(get("/img/champions/{id}/spell/{img}", azir.getId(), spell.getKey() + "." + fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", azir.getLastModifiedDate().getTime()));
	}

	@Test
	public void championPassiveImg() throws Exception {
		Champion ekko = championsResponse.getChampions().get("Ekko");
		ekko = championsRepository.save(ekko);

		String fileExt = FilenameUtils.getExtension(ekko.getPassive().getImage().getFull());

		mockMvc.perform(get("/img//champions/{id}/passive/{passive}.*", ekko.getId(),
				ekko.getPassive().getImage().getFull() + "." + fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", ekko.getLastModifiedDate().getTime()));
	}

	@Test
	public void mapImg() throws Exception {
		GameMap summonersRift = mapsResponse.getMaps().get(SUMMONERS_RIFT);
		summonersRift = mapsRepository.save(summonersRift);

		String fileExt = FilenameUtils.getExtension(summonersRift.getImage().getFull());

		mockMvc.perform(get("/img/maps/map{img}", summonersRift.getMapId() + "." + fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt))
				.andExpect(header().string("Cache-Control", is("max-age=" + 31556926)))
				.andExpect(header().dateValue("Last-Modified", summonersRift.getLastModifiedDate().getTime()));
	}

}