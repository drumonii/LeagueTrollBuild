package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.model.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Response class of a retrieval of LoL Items from Riot's static-data API. Response example:
 * <pre>
 *  {
 *      "type": "item",
 *      "version": "5.16.1",
 *      "data": {
 *          "3711": {
 *              id": 3711,
 *              "name": "Poacher's Knife",
 *              "group": "JungleItems",
 *              "consumed": null,
 *              "description": "...",
 *              "from": [
 *                  "1039"
 *              ],
 *              "into": [
 *                  "3719",
 *                  "3720",
 *                  "3721",
 *                  "3722"
 *              ],
 *              "image": {
 *                  "full": "3711.png",
 *                  "sprite": "item2.png",
 *                  "group": "item",
 *                  "w": 48,
 *                  "h": 48,
 *                  "x": 432,
 *                  "y": 192
 *              },
 *              "gold": {
 *                  "id": 3711,
 *                  "base": 450,
 *                  "total": 850,
 *                  "sell": 595,
 *                  "purchasable": true
 *              }
 *          }
 *      }
 *  }
 * </pre>
 * <b>Note</b>: Not all attributes are retrieved from Riot, only the ones that are used.
 */
@NoArgsConstructor
public class ItemsResponse extends BaseResponse {

	public ItemsResponse(String type, String version) {
		super(type, version);
	}

	@JsonProperty("data")
	@Getter @Setter private Map<String, Item> items;

}
