package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.model.Champion;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Response class of a retrieval of LoL {@link Champion}s from Riot's {@code Data Dragon} API.
 * Response example:
 *
 * <pre>
 *  {
 *      "type": "champion",
 *      "version": "5.16.1",
 *      "data": {
 *          "Thresh": {
 *          	"id": 412,
 *          	"key": "Thresh",
 *          	"name": "Thresh",
 *          	"title": "the Chain Warden",
 *          	"image": {
 *              	"full": "Thresh.png",
 *              	"sprite": "champion3.png",
 *              	"group": "champion",
 *              	"x": 336,
 *              	"y": 0,
 *              	"w": 48,
 *              	"h": 48
 *            	},
 *          	"tags": [
 *              	"Support",
 *              	"Fighter"
 *              ],
 *              "partype": "Mana"
 *          }
 *      }
 *  }
 * </pre>
 *
 * <b>Note</b>: Not all attributes are retrieved from Riot, only the ones that are used.
 */
public class ChampionsResponse extends BaseResponse {

	@JsonProperty("data")
	private Map<String, Champion> champions = new LinkedHashMap<>();

	public Map<String, Champion> getChampions() {
		return champions;
	}

	public void setChampions(Map<String, Champion> champions) {
		this.champions = champions;
	}

}
