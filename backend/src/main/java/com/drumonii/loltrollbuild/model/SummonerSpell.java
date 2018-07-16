package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.SummonerSpellImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.SortedSet;

/**
 * League of Legend Summoner Spell.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Summoner">Summoner Spell Wiki</a>
 */
@Entity
@Table(name = "SUMMONER_SPELL")
@EntityListeners(AuditingEntityListener.class)
public class SummonerSpell implements Serializable {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@JsonProperty("id")
	private int id;

	@Version
	@Column(name = "VERSION", nullable = false)
	@JsonIgnore
	private Long version;

	@Column(name = "NAME", nullable = false)
	@JsonProperty("name")
	private String name;

	@Column(name = "DESCRIPTION", nullable = false)
	@JsonProperty("description")
	private String description;

	@Column(name = "LAST_MODIFIED_DATE", nullable = false)
	@LastModifiedDate
	@JsonIgnore
	private LocalDateTime lastModifiedDate;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "summonerSpell")
	@JsonManagedReference
	@JsonProperty("image")
	private SummonerSpellImage image;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "SUMMONER_SPELL_COOLDOWN", joinColumns = @JoinColumn(name = "SUMMONER_SPELL_ID"))
	@Column(name = "COOLDOWN")
	@OrderBy("COOLDOWN ASC")
	@JsonProperty("cooldown")
	private SortedSet<Integer> cooldown;

	@Column(name = "KEY", nullable = false)
	@JsonProperty("key")
	private String key;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "SUMMONER_SPELL_MODE", joinColumns = @JoinColumn(name = "SUMMONER_SPELL_ID"))
	@Enumerated(EnumType.STRING)
	@Column(name = "MODE")
	@OrderBy("MODE ASC")
	@JsonProperty("modes")
	private SortedSet<GameMode> modes;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public SummonerSpellImage getImage() {
		return image;
	}

	public void setImage(SummonerSpellImage image) {
		this.image = image;
	}

	public SortedSet<Integer> getCooldown() {
		return cooldown;
	}

	public void setCooldown(SortedSet<Integer> cooldown) {
		this.cooldown = cooldown;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public SortedSet<GameMode> getModes() {
		return modes;
	}

	public void setModes(SortedSet<GameMode> modes) {
		this.modes = modes;
	}

	public enum GameMode {

		ARAM, CLASSIC, TUTORIAL

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SummonerSpell that = (SummonerSpell) o;
		return id == that.id &&
				Objects.equals(name, that.name) &&
				Objects.equals(description, that.description) &&
				Objects.equals(image, that.image) &&
				Objects.equals(cooldown, that.cooldown) &&
				Objects.equals(key, that.key) &&
				Objects.equals(modes, that.modes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description, image, cooldown, key, modes);
	}

	@Override
	public String toString() {
		return "SummonerSpell{" +
				"id=" + id +
				", version=" + version +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", lastModifiedDate=" + lastModifiedDate +
				", image=" + image +
				", cooldown=" + cooldown +
				", key='" + key + '\'' +
				", modes=" + modes +
				'}';
	}

}
