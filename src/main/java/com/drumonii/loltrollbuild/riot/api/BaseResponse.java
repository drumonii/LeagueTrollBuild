package com.drumonii.loltrollbuild.riot.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base response class from Riot's {@code lol-static-data-v3} or {@code Data Dragon} API which contains common elements
 * among retrievals.
 */
@NoArgsConstructor
@AllArgsConstructor
abstract class BaseResponse {

	@JsonProperty("type")
	@Getter @Setter private String type;

	@JsonProperty("version")
	@Getter @Setter private String version;

}
