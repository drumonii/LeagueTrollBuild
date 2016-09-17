package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Information of a {@link Champion} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "CHAMPION_INFO")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "id", "champion" })
@ToString(exclude = { "id", "champion" })
public class ChampionInfo implements Serializable {

	@Id
	@Column(name = "CHAMPION_ID", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private int id;

	@Column(name = "ATTACK", nullable = false)
	@JsonProperty("attack")
	@Getter @Setter private int attack;

	@Column(name = "DEFENSE", nullable = false)
	@JsonProperty("defense")
	@Getter @Setter private int defense;

	@Column(name = "MAGIC", nullable = false)
	@JsonProperty("magic")
	@Getter @Setter private int magic;

	@Column(name = "DIFFICULTY", nullable = false)
	@JsonProperty("difficulty")
	@Getter @Setter private int difficulty;

	@MapsId
	@OneToOne(optional = false, mappedBy = "info")
	@JsonBackReference
	@Getter @Setter private Champion champion;

}
