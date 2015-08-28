package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.ChampionImage;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * League of Legends Champion.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Champion">Champion Wiki</a>
 */
@Entity
@Table(name = "CHAMPION")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Champion {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@JsonProperty("id")
	@Getter @Setter private int id;

	@Column(name = "CHAMPION_KEY", nullable = false)
	@JsonProperty("key")
	@Getter @Setter private String key;

	@Column(name = "NAME", nullable = false)
	@JsonProperty("name")
	@Getter @Setter private String name;

	@Column(name = "TITLE", nullable = false)
	@JsonProperty("title")
	@Getter @Setter private String title;

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@PrimaryKeyJoinColumn
	@JsonManagedReference
	@JsonProperty("image")
	@Getter @Setter private ChampionImage image;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CHAMPION_TAG",
			joinColumns = @JoinColumn(name = "CHAMPION_ID"))
	@Column(name = "TAG")
	@JsonProperty("tags")
	@Getter @Setter private Set<String> tags;

	@Column(name = "PARTYPE", nullable = false)
	@JsonProperty("partype")
	@Getter @Setter private String partype;

}
