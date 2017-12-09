package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.ChampionImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

/**
 * League of Legends Champion.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Champion">Champion Wiki</a>
 */
@Entity
@Table(name = "CHAMPION")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "lastModifiedDate")
@ToString
public class Champion implements Serializable {

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

	@Column(name = "PARTYPE", nullable = false)
	@JsonProperty("partype")
	@Getter @Setter private String partype;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "champion")
	@JsonManagedReference
	@JsonProperty("info")
	@Getter @Setter private ChampionInfo info;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "champion")
	@Fetch(FetchMode.SELECT)
	@JsonManagedReference
	@JsonProperty("spells")
	@Getter @Setter private List<ChampionSpell> spells;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "champion")
	@JsonManagedReference
	@JsonProperty("passive")
	@Getter @Setter private ChampionPassive passive;

	@Column(name = "LAST_MODIFIED_DATE", nullable = false)
	@LastModifiedDate
	@JsonIgnore
	@Getter @Setter private LocalDateTime lastModifiedDate;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "champion")
	@JsonManagedReference
	@JsonProperty("image")
	@Getter @Setter private ChampionImage image;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CHAMPION_TAG", joinColumns = @JoinColumn(name = "CHAMPION_ID"))
	@Column(name = "TAG")
	@OrderBy("TAG ASC")
	@JsonProperty("tags")
	@Getter @Setter private SortedSet<String> tags;

	// Hibernate's PersistentBag does not honor equals()

	@PostPersist
	public void postPersist() {
		spells = new ArrayList<>(spells);
	}

	@PostLoad
	public void postLoad() {
		spells = new ArrayList<>(spells);
	}

	@PostUpdate
	public void postUpdate() {
		spells = new ArrayList<>(spells);
	}

}
