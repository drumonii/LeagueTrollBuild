package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * League of Legends Champion.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Champion">Champion Wiki</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Champion {

	@JsonProperty("id")
	@Getter @Setter private int id;

	@JsonProperty("key")
	@Getter @Setter private String key;

	@JsonProperty("name")
	@Getter @Setter private String name;

	@JsonProperty("title")
	@Getter @Setter private String title;

	@JsonProperty("image")
	@Getter @Setter private Image image;

	@JsonProperty("tags")
	@Getter @Setter private List<String> tags;

	@JsonProperty("partype")
	@Getter @Setter private String partype;

}
