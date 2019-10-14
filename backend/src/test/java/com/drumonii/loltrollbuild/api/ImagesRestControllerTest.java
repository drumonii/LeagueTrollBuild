package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.riot.service.ImageService;
import com.drumonii.loltrollbuild.test.api.WebMvcRestTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcRestTest(value = ImagesRestController.class,
		includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { ImageService.class }))
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

	@Value("${api.base-path}")
	private String apiPath;

	protected ChampionsResponse championsResponse;
	protected ItemsResponse itemsResponse;
	protected MapsResponse mapsResponse;
	protected SummonerSpellsResponse summonerSpellsResponse;

	public abstract void before();

	@Test
	public void summonerSpellImg() throws Exception {
		mockMvc.perform(get("{apiPath}/img/summoner-spells/{img}", apiPath, 0))
				.andExpect(status().isNotFound());

		SummonerSpell smite = summonerSpellsResponse.getSummonerSpells().get("SummonerSmite");
		smite = summonerSpellsRepository.saveAndFlush(smite);

		String fileExt = getExtension(smite.getImage().getFull());

		mockMvc.perform(get("{apiPath}/img/summoner-spells/{img}", apiPath, smite.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType(fileExt)))
				.andExpect(header().string("Cache-Control", is("max-age=" + getCacheControlMaxAge())));
	}

	@Test
	public void itemImg() throws Exception {
		mockMvc.perform(get("{apiPath}/img/items/{img}", apiPath, 0))
				.andExpect(status().isNotFound());

		Item thornmail = itemsResponse.getItems().get("3075");
		thornmail = itemsRepository.saveAndFlush(thornmail);

		String fileExt = getExtension(thornmail.getImage().getFull());

		mockMvc.perform(get("{apiPath}/img/items/{img}", apiPath, thornmail.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType(fileExt)))
				.andExpect(header().string("Cache-Control", is("max-age=" + getCacheControlMaxAge())));
	}

	@Test
	public void championImg() throws Exception {
		mockMvc.perform(get("{apiPath}/img/champions/{img}", apiPath, 0))
				.andExpect(status().isNotFound());

		Champion shen = championsResponse.getChampions().get("Shen");
		shen = championsRepository.saveAndFlush(shen);

		String fileExt = getExtension(shen.getImage().getFull());

		mockMvc.perform(get("{apiPath}/img/champions/{img}", apiPath, shen.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType(fileExt)))
				.andExpect(header().string("Cache-Control", is("max-age=" + getCacheControlMaxAge())));
	}

	@Test
	public void championSpellImg() throws Exception {
		mockMvc.perform(get("{apiPath}/img/champions/{id}/spell/{img}", apiPath, 0, "key"))
				.andExpect(status().isNotFound());

		Champion azir = championsResponse.getChampions().get("Azir");
		azir = championsRepository.saveAndFlush(azir);
		Optional<ChampionSpell> spell = azir.getSpells().stream().findAny();
		if (spell.isEmpty()) {
			fail("Unable to get a Champion Spell from the Champion");
		}
		String fileExt = getExtension(spell.get().getImage().getFull());

		mockMvc.perform(get("{apiPath}/img/champions/{id}/spell/{img}", apiPath, azir.getId(), spell.get().getKey()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType(fileExt)))
				.andExpect(header().string("Cache-Control", is("max-age=" + getCacheControlMaxAge())));

		mockMvc.perform(get("{apiPath}/img/champions/{id}/spell/{img}", apiPath, azir.getId(), "test." + fileExt))
				.andExpect(status().isNotFound());
	}

	@Test
	public void championPassiveImg() throws Exception {
		mockMvc.perform(get("{apiPath}/img/champions/{id}/passive", apiPath, 0))
				.andExpect(status().isNotFound());

		Champion ekko = championsResponse.getChampions().get("Ekko");
		ekko = championsRepository.saveAndFlush(ekko);

		String fileExt = getExtension(ekko.getPassive().getImage().getFull());

		mockMvc.perform(get("{apiPath}/img//champions/{id}/passive", apiPath, ekko.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType(fileExt)))
				.andExpect(header().string("Cache-Control", is("max-age=" + getCacheControlMaxAge())));
	}

	@Test
	public void mapImg() throws Exception {
		mockMvc.perform(get("{apiPath}/img/maps/{mapId}", apiPath, 0))
				.andExpect(status().isNotFound());

		GameMap summonersRift = mapsResponse.getMaps().get(SUMMONERS_RIFT_SID);
		summonersRift = mapsRepository.saveAndFlush(summonersRift);

		String fileExt = getExtension(summonersRift.getImage().getFull());

		mockMvc.perform(get("{apiPath}/img/maps/{mapId}", apiPath, summonersRift.getMapId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType(fileExt)))
				.andExpect(header().string("Cache-Control", is("max-age=" + getCacheControlMaxAge())));
	}

	/**
	 * Get the image content type without the image file extension dot.
	 *
	 * @param fileExt the file extension
	 * @return the content type
	 */
	private String getContentType(String fileExt) {
		return "image/" + fileExt.substring(fileExt.indexOf('.') + 1);
	}

	/**
	 * Gets the image file extension with the dot.
	 *
	 * @param image the image to get the extension
	 * @return the file extension
	 */
	private String getExtension(String image) {
		return image.substring(image.lastIndexOf('.'));
	}

	/**
	 * Gets the max-age of the Cache-Control header.
	 *
	 * @return the time unit in seconds
	 */
	private long getCacheControlMaxAge() {
		return TimeUnit.DAYS.toSeconds(365L);
	}

}
