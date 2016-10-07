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
import java.util.ArrayList;
import java.util.Date;
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

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@PrimaryKeyJoinColumn
	@JsonManagedReference
	@JsonProperty("info")
	@Getter @Setter private ChampionInfo info;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "champion")
	@Fetch(FetchMode.SELECT)
	@JsonManagedReference
	@JsonProperty("spells")
	@Getter @Setter private List<ChampionSpell> spells;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "champion")
	@JsonManagedReference
	@JsonProperty("passive")
	@Getter @Setter private ChampionPassive passive;

	@Column(name = "LAST_MODIFIED_DATE", nullable = false)
	@LastModifiedDate
	@JsonIgnore
	@Getter @Setter private Date lastModifiedDate;

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@PrimaryKeyJoinColumn
	@JsonManagedReference
	@JsonProperty("image")
	@Getter @Setter private ChampionImage image;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CHAMPION_TAG", joinColumns = @JoinColumn(name = "CHAMPION_ID"))
	@Column(name = "TAG")
	@OrderBy("TAG ASC")
	@JsonProperty("tags")
	@Getter @Setter private SortedSet<String> tags;

	@JsonIgnore
	public boolean isViktor() {
		return name.equals("Viktor");
	}

	@PostPersist
	public void postPersist() {
		// Hibernate's PersistentBag does not honor equals()
		spells = new ArrayList<>(spells);
		for (ChampionSpell spell : spells) {
			spell.setCosts(new ArrayList<>(spell.getCosts()));
			spell.setCooldowns(new ArrayList<>(spell.getCooldowns()));
			spell.setRange(new ArrayList<>(spell.getRange()));
		}
	}

	@PostLoad
	public void postLoad() {
		// Hibernate's PersistentBag does not honor equals()
		spells = new ArrayList<>(spells);
		for (ChampionSpell spell : spells) {
			spell.setCosts(new ArrayList<>(spell.getCosts()));
			spell.setCooldowns(new ArrayList<>(spell.getCooldowns()));
			spell.setRange(new ArrayList<>(spell.getRange()));
		}
	}

	@PostUpdate
	public void postUpdate() {
		// Hibernate's PersistentBag does not honor equals()
		spells = new ArrayList<>(spells);
		for (ChampionSpell spell : spells) {
			spell.setCosts(new ArrayList<>(spell.getCosts()));
			spell.setCooldowns(new ArrayList<>(spell.getCooldowns()));
			spell.setRange(new ArrayList<>(spell.getRange()));
		}
	}

}
