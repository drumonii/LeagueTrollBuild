package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.api.view.ApiViews;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Information of a {@link Champion} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "CHAMPION_INFO")
public class ChampionInfo implements Serializable {

	@Id
	@Column(name = "CHAMPION_ID", unique = true, nullable = false)
	@JsonIgnore
	private int id;

	@Column(name = "ATTACK", nullable = false)
	@JsonProperty
	@JsonView({ ApiViews.AllApis.class })
	private int attack;

	@Column(name = "DEFENSE", nullable = false)
	@JsonProperty
	@JsonView({ ApiViews.AllApis.class })
	private int defense;

	@Column(name = "MAGIC", nullable = false)
	@JsonProperty
	@JsonView({ ApiViews.AllApis.class })
	private int magic;

	@Column(name = "DIFFICULTY", nullable = false)
	@JsonProperty
	@JsonView({ ApiViews.AllApis.class })
	private int difficulty;

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

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public Champion getChampion() {
		return champion;
	}

	public void setChampion(Champion champion) {
		this.champion = champion;
		setId(champion.getId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ChampionInfo that = (ChampionInfo) o;
		return attack == that.attack &&
				defense == that.defense &&
				magic == that.magic &&
				difficulty == that.difficulty;
	}

	@Override
	public int hashCode() {
		return Objects.hash(attack, defense, magic, difficulty);
	}

	@Override
	public String toString() {
		return "ChampionInfo{" +
				"attack=" + attack +
				", defense=" + defense +
				", magic=" + magic +
				", difficulty=" + difficulty +
				'}';
	}

}
