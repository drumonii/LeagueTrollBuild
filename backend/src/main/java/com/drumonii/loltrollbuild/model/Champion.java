package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.api.view.ApiViews;
import com.drumonii.loltrollbuild.model.image.ChampionImage;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Version;
import javax.persistence.*;
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
	private int id;

	@Version
	@Column(name = "VERSION", nullable = false)
	@JsonIgnore
	private Long version;

	@Column(name = "CHAMPION_KEY", nullable = false)
	private String key;

	@Column(name = "NAME", nullable = false)
	@JsonProperty
	@JsonView({ ApiViews.AllApis.class })
	private String name;

	@Column(name = "TITLE", nullable = false)
	@JsonProperty
	@JsonView({ ApiViews.AllApis.class })
	private String title;

	@Column(name = "PARTYPE", nullable = false)
	@JsonProperty
	@JsonView({ ApiViews.AllApis.class })
	private String partype;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "champion")
	@JsonManagedReference
	@JsonProperty
	@JsonView({ ApiViews.RiotDdragonApi.class })
	private ChampionInfo info;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "champion")
	@Fetch(FetchMode.SELECT)
	@JsonManagedReference
	@JsonProperty
	@JsonView({ ApiViews.RiotDdragonApi.class })
	private List<ChampionSpell> spells;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "champion")
	@JsonManagedReference
	@JsonProperty
	@JsonView({ ApiViews.RiotDdragonApi.class })
	private ChampionPassive passive;

	@Column(name = "LAST_MODIFIED_DATE", nullable = false)
	@LastModifiedDate
	@JsonIgnore
	private LocalDateTime lastModifiedDate;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "champion")
	@JsonManagedReference
	@JsonProperty
	@JsonView({ ApiViews.RiotDdragonApi.class })
	private ChampionImage image;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CHAMPION_TAG", joinColumns = @JoinColumn(name = "CHAMPION_ID"))
	@Column(name = "TAG")
	@OrderBy("TAG ASC")
	@JsonProperty
	@JsonView({ ApiViews.AllApis.class })
	private SortedSet<String> tags;

	@JsonGetter("id")
	@JsonView({ ApiViews.AllApis.class })
	public int getId() {
		return id;
	}

	@JsonSetter("key")
	@JsonView({ ApiViews.AllApis.class })
	public void setId(int id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@JsonGetter("key")
	@JsonView({ ApiViews.AllApis.class })
	public String getKey() {
		return key;
	}

	@JsonSetter("id")
	@JsonView({ ApiViews.AllApis.class })
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
		if (spells != null) {
			spells = new ArrayList<>(spells);
		}
	}

	@PostLoad
	public void postLoad() {
		if (spells != null) {
			spells = new ArrayList<>(spells);
		}
	}

	@PostUpdate
	public void postUpdate() {
		if (spells != null) {
			spells = new ArrayList<>(spells);
		}
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
