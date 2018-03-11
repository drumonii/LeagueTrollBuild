package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.ChampionSpell;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Specific {@link Image} of a {@link Champion}'s {@link ChampionSpell} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "CHAMPION_SPELL_IMAGE")
@NoArgsConstructor
@AllArgsConstructor
public class ChampionSpellImage extends Image implements Serializable {

	@Id
	@Column(name = "CHAMPION_SPELL_KEY", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private String key;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CHAMPION_SPELL_KEY")
	@JsonBackReference
	@Getter @Setter private ChampionSpell spell;

}
