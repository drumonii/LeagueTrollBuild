package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import com.drumonii.loltrollbuild.riot.service.SummonerSpellsService;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link RestController} which retrieves the list of {@link SummonerSpell} from Riot's {@code lol-static-data-v3}
 * API with the {@code /riot/summoner-spells} URL mapping.
 */
@RestController
@RequestMapping("/riot/summoner-spells")
public class SummonerSpellsRetrieval {

	@Autowired
	private SummonerSpellsService summonerSpellsService;

	@Autowired
	@Qualifier("summonerSpellsImg")
	private UriComponentsBuilder summonerSpellsImgUri;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	@Autowired
	private VersionsService versionsService;

	/**
	 * Returns the {@link List} of {@link SummonerSpell} from Riot for the most current patch.
	 *
	 * @return the {@link List} of {@link SummonerSpell} from Riot
	 */
	@GetMapping
	public Collection<SummonerSpell> summonerSpells() {
		return summonerSpellsService.getSummonerSpells();
	}

	/**
	 * Persists the {@link List} of {@link SummonerSpell} from Riot for the most current patch and saves their images.
	 * If Summoner Spells already exist in the database, then only the difference (list from Riot not in the database)
	 * is persisted. And if Summoner Spells not found in Riot exist in the database, then those Summoner Spells are
	 * deleted along with their images.
	 * <p></p>
	 * If the {@code truncate} request parameter is set to {@code true}, then all previous Summoner Spells and their
	 * images are deleted and all the ones from Riot are persisted along with their images saved.
	 *
	 * @param truncate (optional) if {@code true}, all previous {@link SummonerSpell}s and their images are deleted and
	 * all the ones from Riot are persisted along with their images saved
	 * @return the {@link List} of {@link SummonerSpell} that are persisted to the database
	 */
	@PostMapping
	public List<SummonerSpell> saveSummonerSpells(@RequestParam(required = false) boolean truncate) {
		List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells();

		if (truncate) {
			summonerSpellsRepository.deleteAll();
		} else {
			List<SummonerSpell> summonerSpellsFromDb = summonerSpellsRepository.findAll();
			List<SummonerSpell> deletedSummonerSpells = ListUtils.subtract(summonerSpellsFromDb, summonerSpells);
			summonerSpellsRepository.delete(deletedSummonerSpells);
			summonerSpells = ListUtils.subtract(summonerSpells, summonerSpellsFromDb);
		}

		Version latestVersion = versionsService.getLatestVersion();

		imageFetcher.setImgsSrcs(summonerSpells.stream().map(SummonerSpell::getImage).collect(Collectors.toList()),
				summonerSpellsImgUri, latestVersion);
		return summonerSpellsRepository.save(summonerSpells);
	}

	/**
	 * Returns a {@link SummonerSpell} from Riot by its ID for the most current patch.
	 *
	 * @param id the ID to lookup the {@link SummonerSpell} from Riot
	 * @return the {@link SummonerSpell} from Riot
	 */
	@GetMapping(value = "/{id}")
	public SummonerSpell summonerSpell(@PathVariable int id) {
		SummonerSpell summonerSpell = summonerSpellsService.getSummonerSpell(id);
		if (summonerSpell == null) {
			throw new ResourceNotFoundException("Could not find Summoner Spell with ID: " + id);
		}
		return summonerSpell;
	}

	/**
	 * Persists a {@link SummonerSpell} from Riot by its ID for the most current patch and saves its image. If the
	 * Summoner Spell already exists in the database, then the previous Summoner Spell and its image are deleted and
	 * the one retrieved from Riot is persisted along with its image saved.
	 *
	 * @param id the ID to lookup the {@link SummonerSpell} from Riot
	 * @return the persisted {@link SummonerSpell}
	 */
	@PostMapping(value = "/{id}")
	public SummonerSpell saveSummonerSpell(@PathVariable int id) {
		SummonerSpell summonerSpell = summonerSpellsService.getSummonerSpell(id);
		if (summonerSpell == null) {
			throw new ResourceNotFoundException("Could not find Summoner Spell with ID: " + id);
		}

		SummonerSpell existing = summonerSpellsRepository.findOne(id);
		if (existing != null) {
			summonerSpellsRepository.delete(id);
		}

		Version latestVersion = versionsService.getLatestVersion();

		imageFetcher.setImgSrc(summonerSpell.getImage(), summonerSpellsImgUri, latestVersion);
		return summonerSpellsRepository.save(summonerSpell);
	}

}
