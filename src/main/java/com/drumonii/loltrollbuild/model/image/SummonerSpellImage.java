package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.rest.core.annotation.RestResource;

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

	@Id
	@Column(name = "SUMMONER_SPELL_ID", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private int id;

	@MapsId
	@OneToOne(optional = false, mappedBy = "image")
	@JsonBackReference
	@RestResource(exported = false)
	@Getter @Setter private SummonerSpell summonerSpell;

}
