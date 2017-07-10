package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.model.GameMap;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Response class of a retrieval of LoL {@link GameMap}s from Riot's {@code lol-static-data-v3} API. Response example:
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
@NoArgsConstructor
@AllArgsConstructor
public class MapsResponse extends BaseResponse {

	@JsonProperty("data")
	@Getter @Setter private Map<String, GameMap> maps = new LinkedHashMap<>();

}
