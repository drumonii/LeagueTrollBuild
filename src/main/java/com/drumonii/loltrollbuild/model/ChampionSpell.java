package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.ChampionSpellImage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@PrimaryKeyJoinColumn
	@JsonManagedReference
	@JsonProperty("image")
	@Getter @Setter private ChampionSpellImage image;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CHAMPION_SPELL_COST", joinColumns = @JoinColumn(name = "CHAMPION_SPELL_KEY"))
	@Fetch(value = FetchMode.SELECT)
	@Column(name = "COST")
	@JsonProperty("cost")
	@Getter @Setter private List<Integer> costs;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CHAMPION_SPELL_COOLDOWN", joinColumns = @JoinColumn(name = "CHAMPION_SPELL_KEY"))
	@Fetch(value = FetchMode.SELECT)
	@Column(name = "COOLDOWN")
	@JsonProperty("cooldown")
	@Getter @Setter private List<Integer> cooldowns;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CHAMPION_SPELL_RANGE", joinColumns = @JoinColumn(name = "CHAMPION_SPELL_KEY"))
	@Fetch(value = FetchMode.SELECT)
	@Column(name = "RANGE")
	@JsonProperty("range")
	@Getter @Setter private List<Integer> range;

	@ManyToOne(optional = false)
	@JsonBackReference
	@RestResource(exported = false)
	@Getter @Setter private Champion champion;

}
