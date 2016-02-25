package com.drumonii.loltrollbuild.model.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Image of a model.
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "imgSrc" })
@ToString
public class Image implements Serializable {

	@Column(name = "IMG_FULL", unique = true, nullable = false)
	@JsonProperty("full")
	@Getter @Setter String full;

	@Column(name = "SPRITE", nullable = false)
	@JsonProperty("sprite")
	@Getter @Setter String sprite;

	@Column(name = "IMG_GROUP", nullable = false)
	@JsonProperty("group")
	@Getter @Setter String group;

	@Column(name = "IMG_SRC", columnDefinition = "BYTEA", nullable = false)
	@JsonIgnore
	@Getter @Setter byte[] imgSrc = new byte[0];

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
