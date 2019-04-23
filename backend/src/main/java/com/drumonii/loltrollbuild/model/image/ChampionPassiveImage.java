package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.ChampionPassive;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		ChampionPassiveImage that = (ChampionPassiveImage) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), id);
	}

}
