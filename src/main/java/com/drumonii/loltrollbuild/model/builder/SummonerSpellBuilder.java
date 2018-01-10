package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.drumonii.loltrollbuild.model.image.SummonerSpellImage;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Builder for {@link SummonerSpell}s.
 */
public final class SummonerSpellBuilder {

	private int id;
	private String name;
	private String description;
	private SummonerSpellImage image;
	private SortedSet<Integer> cooldown;
	private String key;
	private SortedSet<GameMode> modes = new TreeSet<>();

	public SummonerSpellBuilder withId(int id) {
		this.id = id;
		return this;
	}

	public SummonerSpellBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public SummonerSpellBuilder withDescription(String description) {
		this.description = description;
		return this;
	}

	public SummonerSpellBuilder withImage(SummonerSpellImage image) {
		this.image = image;
		return this;
	}

	public SummonerSpellBuilder withCooldown(SortedSet<Integer> cooldown) {
		this.cooldown = cooldown;
		return this;
	}

	public SummonerSpellBuilder withCooldown(Integer... cooldown) {
		this.cooldown = new TreeSet<>(Arrays.asList(cooldown));
		return this;
	}

	public SummonerSpellBuilder withKey(String key) {
		this.key = key;
		return this;
	}

	public SummonerSpellBuilder withModes(SortedSet<GameMode> modes) {
		this.modes = modes;
		return this;
	}

	public SummonerSpellBuilder withModes(GameMode... modes) {
		this.modes = new TreeSet<>(Arrays.asList(modes));
		return this;
	}

	public SummonerSpellBuilder withModes(String... modes) {
		SortedSet<GameMode> gameModes = new TreeSet<>();
		for (String mode : modes) {
			try {
				gameModes.add(GameMode.valueOf(mode));
			} catch (IllegalArgumentException e) {
				// Ignore the game mode if can't be parsed
			}
		}
		this.modes = gameModes;
		return this;
	}

	public SummonerSpell build() {
		SummonerSpell summonerSpell = new SummonerSpell();
		summonerSpell.setId(id);
		summonerSpell.setName(name);
		summonerSpell.setDescription(description);
		summonerSpell.setImage(image);
		if (image != null) {
			image.setSummonerSpell(summonerSpell);
		}
		summonerSpell.setCooldown(cooldown);
		summonerSpell.setKey(key);
		summonerSpell.setModes(modes);
		return summonerSpell;
	}

}
