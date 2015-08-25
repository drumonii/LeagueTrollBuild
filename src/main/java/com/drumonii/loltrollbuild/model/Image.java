package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Image of a model.
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Image {

	@JsonProperty("full")
	@Getter @Setter String full;

	@JsonProperty("sprite")
	@Getter @Setter String sprite;

	@JsonProperty("group")
	@Getter @Setter String group;

	@JsonProperty("x")
	@Getter @Setter int x;

	@JsonProperty("y")
	@Getter @Setter int y;

	@JsonProperty("w")
	@Getter @Setter int w;

	@JsonProperty("h")
	@Getter @Setter int h;

}
