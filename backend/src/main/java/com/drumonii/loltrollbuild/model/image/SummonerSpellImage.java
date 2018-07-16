package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Specific {@link Image} of a {@link SummonerSpell} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "SUMMONER_SPELL_IMAGE")
public class SummonerSpellImage extends Image implements Serializable {

	@Id
	@Column(name = "SUMMONER_SPELL_ID", unique = true, nullable = false)
	@JsonIgnore
	private int id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	@JsonBackReference
	private SummonerSpell summonerSpell;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SummonerSpell getSummonerSpell() {
		return summonerSpell;
	}

	public void setSummonerSpell(SummonerSpell summonerSpell) {
		this.summonerSpell = summonerSpell;
	}

}
