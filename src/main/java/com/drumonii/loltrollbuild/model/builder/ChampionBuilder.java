package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.ChampionInfo;
import com.drumonii.loltrollbuild.model.ChampionPassive;
import com.drumonii.loltrollbuild.model.ChampionSpell;
import com.drumonii.loltrollbuild.model.image.ChampionImage;

import java.util.*;

/**
 * Builder for {@link Champion}s.
 */
public final class ChampionBuilder {

	private int id;
	private String key;
	private String name;
	private String title;
	private String partype;
	private ChampionInfo info;
	private List<ChampionSpell> spells = new ArrayList<>();
	private ChampionPassive passive;
	private ChampionImage image;
	private SortedSet<String> tags = new TreeSet<>();

	public ChampionBuilder withId(int id) {
		this.id = id;
		return this;
	}

	public ChampionBuilder withKey(String key) {
		this.key = key;
		return this;
	}

	public ChampionBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public ChampionBuilder withTitle(String title) {
		this.title = title;
		return this;
	}

	public ChampionBuilder withPartype(String partype) {
		this.partype = partype;
		return this;
	}

	public ChampionBuilder withInfo(ChampionInfo info) {
		this.info = info;
		return this;
	}

	public ChampionBuilder withSpells(List<ChampionSpell> spells) {
		this.spells = spells;
		return this;
	}

	public ChampionBuilder withSpells(ChampionSpell... spells) {
		this.spells = Arrays.asList(spells);
		return this;
	}

	public ChampionBuilder withPassive(ChampionPassive passive) {
		this.passive = passive;
		return this;
	}

	public ChampionBuilder withImage(ChampionImage image) {
		this.image = image;
		return this;
	}

	public ChampionBuilder withTags(SortedSet<String> tags) {
		this.tags = tags;
		return this;
	}

	public ChampionBuilder withTags(String... tags) {
		this.tags = new TreeSet<>(Arrays.asList(tags));
		return this;
	}

	public Champion build() {
		Champion champion = new Champion();
		champion.setId(id);
		champion.setKey(key);
		champion.setName(name);
		champion.setTitle(title);
		champion.setPartype(partype);
		champion.setInfo(info);
		if (info != null) {
			info.setChampion(champion);
		}
		champion.setSpells(spells);
		if (spells != null) {
			for (ChampionSpell spell : spells) {
				spell.setChampion(champion);
			}
		}
		champion.setPassive(passive);
		if (passive != null) {
			passive.setChampion(champion);
		}
		champion.setImage(image);
		if (image != null) {
			image.setChampion(champion);
		}
		champion.setTags(tags);
		return champion;
	}

}
