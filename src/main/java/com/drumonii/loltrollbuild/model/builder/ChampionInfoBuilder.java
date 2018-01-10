package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.ChampionInfo;

/**
 * Builder for {@link ChampionInfo}.
 */
public final class ChampionInfoBuilder {

	private int attack;
	private int defense;
	private int magic;
	private int difficulty;

	public ChampionInfoBuilder withAttack(int attack) {
		this.attack = attack;
		return this;
	}

	public ChampionInfoBuilder withDefense(int defense) {
		this.defense = defense;
		return this;
	}

	public ChampionInfoBuilder withMagic(int magic) {
		this.magic = magic;
		return this;
	}

	public ChampionInfoBuilder withDifficulty(int difficulty) {
		this.difficulty = difficulty;
		return this;
	}

	public ChampionInfo build() {
		ChampionInfo championInfo = new ChampionInfo();
		championInfo.setAttack(attack);
		championInfo.setDefense(defense);
		championInfo.setMagic(magic);
		championInfo.setDifficulty(difficulty);
		return championInfo;
	}

}
