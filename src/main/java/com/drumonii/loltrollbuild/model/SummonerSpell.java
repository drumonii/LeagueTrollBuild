package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * League of Legend Summoner Spell.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Summoner">Summoner Spell Wiki</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SummonerSpell {

	@JsonProperty("id")
	@Getter @Setter private int id;

	@JsonProperty("name")
	@Getter @Setter private String name;

	@JsonProperty("description")
	@Getter @Setter private String description;

	@JsonProperty("image")
	@Getter @Setter private Image image;

	@JsonProperty("cooldown")
	@Getter @Setter private List<Integer> cooldown;

	@JsonProperty("modes")
	@Getter @Setter private List<String> modes;

}
