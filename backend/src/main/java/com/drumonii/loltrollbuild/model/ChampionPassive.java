package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.api.view.ApiViews;
import com.drumonii.loltrollbuild.model.image.ChampionPassiveImage;
import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * {@link Champion}'s passive which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "CHAMPION_PASSIVE")
public class ChampionPassive implements Serializable {

	@Id
	@Column(name = "CHAMPION_ID", unique = true, nullable = false)
	@JsonIgnore
	private int id;

	@Column(name = "NAME", nullable = false)
	@JsonProperty
	@JsonView({ ApiViews.AllApis.class })
	private String name;

	@Column(name = "DESCRIPTION", nullable = false)
	@JsonProperty
	@JsonView({ ApiViews.AllApis.class })
	private String description;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "passive")
	@JsonManagedReference
	@JsonProperty
	@JsonView({ ApiViews.RiotDdragonApi.class })
	private ChampionPassiveImage image;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	@JsonBackReference
	private Champion champion;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public ChampionPassiveImage getImage() {
		return image;
	}

	public void setImage(ChampionPassiveImage image) {
		this.image = image;
	}

	public Champion getChampion() {
		return champion;
	}

	public void setChampion(Champion champion) {
		this.champion = champion;
		setId(champion.getId());
		image.setId(champion.getId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ChampionPassive that = (ChampionPassive) o;
		return Objects.equals(name, that.name) &&
				Objects.equals(description, that.description) &&
				Objects.equals(image, that.image);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, image);
	}

	@Override
	public String toString() {
		return "ChampionPassive{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", image=" + image +
				'}';
	}

}
