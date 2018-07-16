package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.Champion;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Specific {@link Image} of a {@link Champion} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "CHAMPION_IMAGE")
public class ChampionImage extends Image implements Serializable {

	@Id
	@Column(name = "CHAMPION_ID", unique = true, nullable = false)
	@JsonIgnore
	private int id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	@JsonBackReference
	private Champion champion;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Champion getChampion() {
		return champion;
	}

	public void setChampion(Champion champion) {
		this.champion = champion;
	}

}
