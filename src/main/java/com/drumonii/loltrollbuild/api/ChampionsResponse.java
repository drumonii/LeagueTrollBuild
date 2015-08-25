package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.model.Champion;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Response class of a retrieval of LoL Champions from Riot's static-data API. Response example:
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
 * <b>Note</b>: Not all attributes are retrieved from Riot, only the ones that are used.
 */
@NoArgsConstructor
public class ChampionsResponse extends BaseResponse {

	public ChampionsResponse(String type, String version) {
		super(type, version);
	}

	@JsonProperty("data")
	@Getter @Setter private Map<String, Champion> champions;

}
