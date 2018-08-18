package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.ChampionImage;
import com.drumonii.loltrollbuild.rest.view.ApiViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;

/**
 * League of Legends Champion.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Champion">Champion Wiki</a>
 */
@Entity
@Table(name = "CHAMPION")
@EntityListeners(AuditingEntityListener.class)
public class Champion implements Serializable {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@JsonProperty("id")
	@JsonView({ ApiViews.AllApis.class })
	private int id;

	@Version
	@Column(name = "VERSION", nullable = false)
	@JsonIgnore
	private Long version;

	@Column(name = "CHAMPION_KEY", nullable = false)
	@JsonProperty("key")
	@JsonView({ ApiViews.AllApis.class })
	private String key;

	@Column(name = "NAME", nullable = false)
	@JsonProperty("name")
	@JsonView({ ApiViews.AllApis.class })
	private String name;

	@Column(name = "TITLE", nullable = false)
	@JsonProperty("title")
	@JsonView({ ApiViews.AllApis.class })
	private String title;

	@Column(name = "PARTYPE", nullable = false)
	@JsonProperty("partype")
	@JsonView({ ApiViews.AllApis.class })
	private String partype;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "champion")
	@JsonManagedReference
	@JsonProperty("info")
	@JsonView({ ApiViews.RiotApi.class })
	private ChampionInfo info;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "champion")
	@Fetch(FetchMode.SELECT)
	@JsonManagedReference
	@JsonProperty("spells")
	@JsonView({ ApiViews.RiotApi.class })
	private List<ChampionSpell> spells;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "champion")
	@JsonManagedReference
	@JsonProperty("passive")
	@JsonView({ ApiViews.RiotApi.class })
	private ChampionPassive passive;

	@Column(name = "LAST_MODIFIED_DATE", nullable = false)
	@LastModifiedDate
	@JsonIgnore
	private LocalDateTime lastModifiedDate;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "champion")
	@JsonManagedReference
	@JsonProperty("image")
	@JsonView({ ApiViews.RiotApi.class })
	private ChampionImage image;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CHAMPION_TAG", joinColumns = @JoinColumn(name = "CHAMPION_ID"))
	@Column(name = "TAG")
	@OrderBy("TAG ASC")
	@JsonProperty("tags")
	@JsonView({ ApiViews.AllApis.class })
	private SortedSet<String> tags;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPartype() {
		return partype;
	}

	public void setPartype(String partype) {
		this.partype = partype;
	}

	public ChampionInfo getInfo() {
		return info;
	}

	public void setInfo(ChampionInfo info) {
		this.info = info;
	}

	public List<ChampionSpell> getSpells() {
		return spells;
	}

	public void setSpells(List<ChampionSpell> spells) {
		this.spells = spells;
	}

	public ChampionPassive getPassive() {
		return passive;
	}

	public void setPassive(ChampionPassive passive) {
		this.passive = passive;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public ChampionImage getImage() {
		return image;
	}

	public void setImage(ChampionImage image) {
		this.image = image;
	}

	public SortedSet<String> getTags() {
		return tags;
	}

	public void setTags(SortedSet<String> tags) {
		this.tags = tags;
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Champion champion = (Champion) o;
		return id == champion.id &&
				Objects.equals(key, champion.key) &&
				Objects.equals(name, champion.name) &&
				Objects.equals(title, champion.title) &&
				Objects.equals(partype, champion.partype) &&
				Objects.equals(info, champion.info) &&
				Objects.equals(spells, champion.spells) &&
				Objects.equals(passive, champion.passive) &&
				Objects.equals(image, champion.image) &&
				Objects.equals(tags, champion.tags);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, key, name, title, partype, info, spells, passive, image, tags);
	}

	@Override
	public String toString() {
		return "Champion{" +
				"id=" + id +
				", version=" + version +
				", key='" + key + '\'' +
				", name='" + name + '\'' +
				", title='" + title + '\'' +
				", partype='" + partype + '\'' +
				", info=" + info +
				", spells=" + spells +
				", passive=" + passive +
				", lastModifiedDate=" + lastModifiedDate +
				", image=" + image +
				", tags=" + tags +
				'}';
	}

}
