package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.List;

import static com.drumonii.loltrollbuild.util.MapUtil.getElementsFromMap;

/**
 * A {@link RestController} which retrieves the list of {@link SummonerSpell} from Riot's {@code lol-static-data-v1.2}
 * API with the {@code /riot/summoner-spells} URL mapping.
 */
@RestController
@RequestMapping("/riot/summoner-spells")
public class SummonerSpellsRetrieval {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@RequestMapping(method = RequestMethod.GET)
	public List<SummonerSpell> summonerSpells() {
		SummonerSpellsResponse response = restTemplate.getForObject(summonerSpellsUri.toString(),
				SummonerSpellsResponse.class);
		return getElementsFromMap(response.getSummonerSpells());
	}

}
