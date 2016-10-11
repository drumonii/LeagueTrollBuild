package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.ChampionPassiveImage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.io.Serializable;

/**
 * {@link Champion}'s passive which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "CHAMPION_PASSIVE")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "id", "champion" })
@ToString(exclude = { "id", "champion" })
public class ChampionPassive implements Serializable {

	@Id
	@Column(name = "CHAMPION_ID", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private int id;

	@Column(name = "NAME", nullable = false)
	@JsonProperty("name")
	@Getter @Setter private String name;

	@Column(name = "DESCRIPTION", nullable = false)
	@JsonProperty("description")
	@Getter @Setter private String description;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "passive")
	@JsonManagedReference
	@JsonProperty("image")
	@Getter @Setter private ChampionPassiveImage image;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	@JsonBackReference
	@RestResource(exported = false)
	@Getter @Setter private Champion champion;

}
