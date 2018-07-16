package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.ChampionPassive;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Specific {@link Image} of a {@link Champion}'s {@link ChampionPassive} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "CHAMPION_PASSIVE_IMAGE")
public class ChampionPassiveImage extends Image implements Serializable {

	@Id
	@Column(name = "CHAMPION_ID", unique = true, nullable = false)
	@JsonIgnore
	private int id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CHAMPION_ID")
	@JsonBackReference
	private ChampionPassive passive;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ChampionPassive getPassive() {
		return passive;
	}

	public void setPassive(ChampionPassive passive) {
		this.passive = passive;
	}

}
