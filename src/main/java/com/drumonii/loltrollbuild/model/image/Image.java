package com.drumonii.loltrollbuild.model.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Image of a model.
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Image {

	@Column(name = "IMG_FULL", unique = true, nullable = false)
	@JsonProperty("full")
	@Getter @Setter String full;

	@Column(name = "SPRITE", nullable = false)
	@JsonProperty("sprite")
	@Getter @Setter String sprite;

	@Column(name = "IMG_GROUP", nullable = false)
	@JsonProperty("group")
	@Getter @Setter String group;

	@Column(name = "X", nullable = false)
	@JsonProperty("x")
	@Getter @Setter int x;

	@Column(name = "Y", nullable = false)
	@JsonProperty("y")
	@Getter @Setter int y;

	@Column(name = "W", nullable = false)
	@JsonProperty("w")
	@Getter @Setter int w;

	@Column(name = "H", nullable = false)
	@JsonProperty("h")
	@Getter @Setter int h;

}
