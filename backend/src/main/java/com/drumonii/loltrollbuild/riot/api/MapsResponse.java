package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.model.GameMap;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Response class of a retrieval of LoL {@link GameMap}s from Riot's {@code lol-static-data-v3} or {@code Data Dragon} API.
 * Response example:
 *
 * <pre>
 *  {
 *      "type": "map",
 *      "version": "5.24.2",
 *      "data": {
 *          "11": {
 *              "mapName": "SummonersRiftNew",
 *              "mapId": 11,
 *              "image": {
 *                  "full": "map11.png",
 *                  "sprite": "map0.png",
 *                  "group": "map",
 *                  "x": 144,
 *                  "y": 0,
 *                  "w": 48,
 *                  "h": 48
 *              }
 *          }
 *      }
 *  }
 * </pre>
 */
public class MapsResponse extends BaseResponse {

	@JsonProperty("data")
	private Map<String, GameMap> maps = new LinkedHashMap<>();

	public Map<String, GameMap> getMaps() {
		return maps;
	}

	public void setMaps(Map<String, GameMap> maps) {
		this.maps = maps;
	}

}
