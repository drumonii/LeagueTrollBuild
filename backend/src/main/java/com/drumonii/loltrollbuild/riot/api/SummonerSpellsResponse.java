package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Response class of a retrieval of LoL {@link SummonerSpell}s from Riot's {@code Data Dragon} API.
 * Response example:
 *
 * <pre>
 *  {
 *      "type": "summoner",
 *      "version": "5.16.1",
 *      "data": {
 *          "SummonerSmite": {
 *              "name": "Smite",
 *              "description": "...",
 *              "image": {
 *                  "full": "SummonerSmite.png",
 *                  "sprite": "spell0.png",
 *                  "group": "spell",
 *                  "x": 96,
 *                  "y": 48,
 *                  "w": 48,
 *                  "h": 48
 *              },
 *              "cooldown": [
 *                  90
 *              ],
 *              "summonerLevel": 10,
 *              "id": 11,
 *              "key": "SummonerSmite",
 *              "modes": [
 *                  "CLASSIC",
 *                  "TUTORIAL"
 *              ]
 *          }
 *      }
 *  }
 * </pre>
 *
 * <b>Note</b>: Not all attributes are retrieved from Riot, only the ones that are used.
 */
public class SummonerSpellsResponse extends BaseResponse {

	@JsonProperty("data")
	private Map<String, SummonerSpell> summonerSpells = new LinkedHashMap<>();

	public Map<String, SummonerSpell> getSummonerSpells() {
		return summonerSpells;
	}

	public void setSummonerSpells(Map<String, SummonerSpell> summonerSpells) {
		this.summonerSpells = summonerSpells;
	}

}
