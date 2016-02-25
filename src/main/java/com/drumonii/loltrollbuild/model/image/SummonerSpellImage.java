package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Specific {@link Image} of a {@link SummonerSpell} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "SUMMONER_SPELL_IMAGE")
@NoArgsConstructor
@AllArgsConstructor
public class SummonerSpellImage extends Image implements Serializable {

	public SummonerSpellImage(String full, String sprite, String group, byte[] imgSrc, int x, int y, int w, int h) {
		super(full, sprite, group, imgSrc, x, y, w, h);
	}

	@Id
	@Column(name = "SUMMONER_SPELL_ID", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private int id;

	@MapsId
	@OneToOne(optional = false, mappedBy = "image")
	@JsonBackReference
	@Getter @Setter private SummonerSpell summonerSpell;

}
