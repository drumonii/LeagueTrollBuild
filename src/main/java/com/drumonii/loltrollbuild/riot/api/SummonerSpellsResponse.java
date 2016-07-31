package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Response class of a retrieval of LoL {@link SummonerSpell}s from Riot's {@code lol-static-data-v1.2} API. Response
 * example:
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
@NoArgsConstructor
public class SummonerSpellsResponse extends BaseResponse {

	@JsonProperty("data")
	@Getter @Setter private Map<String, SummonerSpell> summonerSpells;

}
