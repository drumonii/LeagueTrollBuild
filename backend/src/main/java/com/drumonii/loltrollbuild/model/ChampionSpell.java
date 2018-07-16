package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.ChampionSpellImage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Spells of a {@link Champion} which maps a {@link ManyToOne} relationship.
 */
@Entity
@Table(name = "CHAMPION_SPELL")
public class ChampionSpell implements Serializable {

	@Id
	@Column(name = "KEY", unique = true, nullable = false)
	@JsonProperty("key")
	private String key;

	@Column(name = "NAME", nullable = false)
	@JsonProperty("name")
	private String name;

	@Column(name = "DESCRIPTION", nullable = false)
	@JsonProperty("description")
	private String description;

	@Column(name = "TOOLTIP", nullable = false)
	@JsonProperty("tooltip")
	private String tooltip = "";

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "spell")
	@JsonManagedReference
	@JsonProperty("image")
	private ChampionSpellImage image;

	@ManyToOne(optional = false)
	@JsonBackReference
	private Champion champion;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public ChampionSpellImage getImage() {
		return image;
	}

	public void setImage(ChampionSpellImage image) {
		this.image = image;
	}

	public Champion getChampion() {
		return champion;
	}

	public void setChampion(Champion champion) {
		this.champion = champion;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ChampionSpell that = (ChampionSpell) o;
		return Objects.equals(key, that.key) &&
				Objects.equals(name, that.name) &&
				Objects.equals(description, that.description) &&
				Objects.equals(tooltip, that.tooltip) &&
				Objects.equals(image, that.image);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, name, description, tooltip, image);
	}

	@Override
	public String toString() {
		return "ChampionSpell{" +
				"key='" + key + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", tooltip='" + tooltip + '\'' +
				", image=" + image +
				'}';
	}

}
