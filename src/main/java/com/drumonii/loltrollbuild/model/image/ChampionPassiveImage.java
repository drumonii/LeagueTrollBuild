package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.ChampionPassive;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Specific {@link Image} of a {@link Champion}'s {@link ChampionPassive} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "CHAMPION_PASSIVE_IMAGE")
@NoArgsConstructor
@AllArgsConstructor
public class ChampionPassiveImage extends Image implements Serializable {

	@Id
	@Column(name = "CHAMPION_ID", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private int id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CHAMPION_ID")
	@JsonBackReference
	@Getter @Setter private ChampionPassive passive;

}
