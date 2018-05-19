package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Troll Build (random build).
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrollBuild {

	@JsonProperty
	@Getter @Setter private List<Item> items;

	@JsonProperty
	@Getter @Setter private List<SummonerSpell> summonerSpells;

	@JsonProperty
	@Getter @Setter private Item trinket;

}
