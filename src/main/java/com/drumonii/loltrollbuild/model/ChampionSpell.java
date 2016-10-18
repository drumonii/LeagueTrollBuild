package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.ChampionSpellImage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Spells of a {@link Champion} which maps a {@link ManyToOne} relationship.
 */
@Entity
@Table(name = "CHAMPION_SPELL")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "champion")
@ToString(exclude = "champion")
public class ChampionSpell implements Serializable {

	@Id
	@Column(name = "KEY", unique = true, nullable = false)
	@JsonProperty("key")
	@Getter @Setter private String key;

	@Column(name = "NAME", nullable = false)
	@JsonProperty("name")
	@Getter @Setter private String name;

	@Column(name = "DESCRIPTION", nullable = false)
	@JsonProperty("description")
	@Getter @Setter private String description;

	@Column(name = "TOOLTIP", nullable = false)
	@JsonProperty("tooltip")
	@Getter @Setter private String tooltip;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "spell")
	@JsonManagedReference
	@JsonProperty("image")
	@Getter @Setter private ChampionSpellImage image;

	@ManyToOne(optional = false)
	@JsonBackReference
	@RestResource(exported = false)
	@Getter @Setter private Champion champion;

}
