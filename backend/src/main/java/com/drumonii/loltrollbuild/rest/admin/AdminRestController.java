package com.drumonii.loltrollbuild.rest.admin;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.service.ChampionsService;
import com.drumonii.loltrollbuild.riot.service.ItemsService;
import com.drumonii.loltrollbuild.riot.service.MapsService;
import com.drumonii.loltrollbuild.riot.service.SummonerSpellsService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Controller for the administration area only used and authorized by the admin user.
 */
@RestController
@RequestMapping("${api.base-path}/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRestController {

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private SummonerSpellsService summonerSpellsService;

	@GetMapping(path = "/summoner-spells/diff")
	public Collection<SummonerSpell> summonerSpellsDifference() {
		return CollectionUtils.subtract(summonerSpellsService.getSummonerSpells(), summonerSpellsRepository.findAll());
	}

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ItemsService itemsService;

	@GetMapping(path = "/items/diff")
	public Collection<Item> itemsDifference() {
		return CollectionUtils.subtract(itemsService.getItems(), itemsRepository.findAll());
	}

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ChampionsService championsService;

	@GetMapping(path = "/champions/diff")
	public Collection<Champion> championsDifference() {
		return CollectionUtils.subtract(championsService.getChampions(), championsRepository.findAll());
	}

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private MapsService mapsService;

	@GetMapping(path = "/maps/diff")
	public Collection<GameMap> mapsDifference() {
		return CollectionUtils.subtract(mapsService.getMaps(), mapsRepository.findAll());
	}

}
