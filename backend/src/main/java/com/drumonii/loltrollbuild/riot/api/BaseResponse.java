package com.drumonii.loltrollbuild.riot.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base response class from Riot's {@code lol-static-data-v3} or {@code Data Dragon} API which contains common elements
 * among retrievals.
 */
abstract class BaseResponse {

	@JsonProperty("type")
	private String type;

	@JsonProperty("version")
	private String version;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
