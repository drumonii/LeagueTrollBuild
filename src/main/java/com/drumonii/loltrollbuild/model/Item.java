package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * League of Legends Item.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Item">Item Wiki</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Item {

	@JsonProperty("id")
	@Getter @Setter private int id;

	@JsonProperty("name")
	@Getter @Setter private String name;

	@JsonProperty("group")
	@Getter @Setter private String group;

	@JsonProperty("consumed")
	@Getter @Setter private Boolean consumed;

	@JsonProperty("description")
	@Getter @Setter private String description;

	@JsonProperty("from")
	@Getter @Setter private List<String> from;

	@JsonProperty("into")
	@Getter @Setter private List<String> into;

	@JsonProperty("maps")
	@Getter @Setter private Map<String, Boolean> maps;

	@JsonProperty("image")
	@Getter @Setter private Image image;

	@JsonProperty("gold")
	@Getter @Setter private ItemGold gold;

}
