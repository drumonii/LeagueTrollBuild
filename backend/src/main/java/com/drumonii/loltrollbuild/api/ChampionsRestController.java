package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.api.service.ChampionsApiService;
import com.drumonii.loltrollbuild.api.service.ItemsApiService;
import com.drumonii.loltrollbuild.api.service.MapsApiService;
import com.drumonii.loltrollbuild.api.service.SummonerSpellsApiService;
import com.drumonii.loltrollbuild.api.status.ResourceNotFoundException;
import com.drumonii.loltrollbuild.api.view.ApiViews;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.TrollBuild;
import com.drumonii.loltrollbuild.model.builder.TrollBuildBuilder;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import com.drumonii.loltrollbuild.util.ChampionUtil;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;

/**
 * Repository REST controller for {@link Champion}s.
 */
@RestController
@RequestMapping("${api.base-path}/champions")
public class ChampionsRestController {

	@Autowired
	private ChampionsApiService championsApiService;

	@Autowired
	private ItemsApiService itemsApiService;

	@Autowired
	private SummonerSpellsApiService summonerSpellsApiService;

	@Autowired
	private MapsApiService mapsApiService;

	/**
	 * Gets a {@link List} of {@link Champion}s from the sort and search parameters.
	 *
	 * @param sort the {@link Sort}
	 * @param champion the search {@link Champion} to define as the QBE
	 * @return the {@link List} of {@link Champion}s
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping
	public List<Champion> getChampions(
			@SortDefault(sort = "name", direction = Direction.ASC) Sort sort, Champion champion) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::contains)
				.withMatcher("title", GenericPropertyMatcher::contains)
				.withMatcher("partype", GenericPropertyMatcher::contains)
				.withIgnoreCase()
				.withIgnorePaths("id", "version")
				.withIgnoreNullValues();
		Example<Champion> example = Example.of(champion, exampleMatcher);
		return championsApiService.qbe(new ExampleSpecification<>(example), sort);
	}

	/**
	 * Gets a {@link Champion} by either its ID or name. If not found, returns a 404, otherwise a 200.
	 *
	 * @param value the value to lookup the {@link Champion}
	 * @return the {@link Champion}
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping(path = "/{value}")
	public Champion getChampion(@PathVariable String value) {
		return championsApiService.find(value)
				.orElseThrow(() -> new ResourceNotFoundException("Unable to find a Champion with value: " + value));
	}

	/**
	 * Gets the distinct {@link List} of {@link Champion} tags.
	 *
	 * @return the {@link List} of tags
	 */
	@GetMapping(path = "/tags")
	public List<String> getTags() {
		return championsApiService.getTags();
	}

	/**
	 * Generates the full troll build based on the specified {@link Champion} and map ID.
	 *
	 * @param value the value to lookup the {@link Champion} to create the troll build for
	 * @param mapId the map ID to generate the troll build
	 * @return a {@link Map} of build type key with {@link List} of values.
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping(path = "/{value}/troll-build")
	public TrollBuild trollBuild(@PathVariable String value,
			@RequestParam(required = false, defaultValue = SUMMONERS_RIFT_SID) int mapId) {
		Champion champion = championsApiService.find(value)
				.orElseThrow(() -> new ResourceNotFoundException("Unable to find a Champion with value: " + value));

		return new TrollBuildBuilder()
				.withBoots(itemsApiService.boots(mapId))
				.withItems(itemsApiService.forTrollBuild(mapId))
				.withSummonerSpells(summonerSpellsApiService.forTrollBuild(GameMapUtil
						.getModeFromMap(mapsApiService.findById(mapId).orElse(null))))
				.withTrinket(itemsApiService.trinkets(mapId))
				.withViktor(ChampionUtil.isViktor(champion) ? itemsApiService.viktorOnly() : null)
				.build();
	}

}
