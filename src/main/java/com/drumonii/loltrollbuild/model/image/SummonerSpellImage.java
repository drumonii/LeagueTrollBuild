package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * Specific {@link Image} of a {@link SummonerSpell} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "SUMMONER_SPELL_IMAGE")
@NoArgsConstructor
@AllArgsConstructor
public class SummonerSpellImage extends Image {

	public SummonerSpellImage(String full, String sprite, String group, int x, int y, int w, int h) {
		super(full, sprite, group, x, y, w, h);
	}

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "foreign",
			parameters = @Parameter(name = "property", value = "summonerSpell"))
	@Column(name = "SUMMONER_SPELL_ID", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private int id;

	@OneToOne(optional = false, mappedBy = "image")
	@JsonBackReference
	@Getter @Setter private SummonerSpell summonerSpell;

}
