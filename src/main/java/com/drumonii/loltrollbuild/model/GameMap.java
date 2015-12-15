package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.GameMapImage;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Map;

/**
 * League of Legends Map. Also called a Field of Justice. Expressed as a "GameMap" to avoid the name space with a
 * {@link Map}.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Field_of_Justice">Field of Justice Wiki</a>
 */
@Entity
@Table(name = "MAP")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class GameMap {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@JsonProperty("mapId")
	@Getter @Setter private int mapId;

	@Column(name = "NAME", nullable = false)
	@JsonProperty("mapName")
	@Getter @Setter private String mapName;

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@PrimaryKeyJoinColumn
	@JsonManagedReference
	@JsonProperty("image")
	@Getter @Setter private GameMapImage image;

	public void useActualMapName() {
		switch (mapName) {
			case "CrystalScar":
				mapName = "Crystal Scar";
				break;
			case "NewTwistedTreeline":
				mapName = "Twisted Treeline";
				break;
			case "SummonersRiftNew":
				mapName = "Summoner's Rift";
				break;
			case "ProvingGroundsNew":
				mapName = "Proving Grounds";
				break;
		}
	}

}
