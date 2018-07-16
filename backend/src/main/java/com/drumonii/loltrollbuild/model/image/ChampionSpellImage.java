package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.ChampionSpell;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Specific {@link Image} of a {@link Champion}'s {@link ChampionSpell} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "CHAMPION_SPELL_IMAGE")
public class ChampionSpellImage extends Image implements Serializable {

	@Id
	@Column(name = "CHAMPION_SPELL_KEY", unique = true, nullable = false)
	@JsonIgnore
	private String key;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CHAMPION_SPELL_KEY")
	@JsonBackReference
	private ChampionSpell spell;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ChampionSpell getSpell() {
		return spell;
	}

	public void setSpell(ChampionSpell spell) {
		this.spell = spell;
	}

}
