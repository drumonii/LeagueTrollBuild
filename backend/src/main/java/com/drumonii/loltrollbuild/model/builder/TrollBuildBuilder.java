package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.TrollBuild;
import com.drumonii.loltrollbuild.util.RandomizeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Builder for {@link TrollBuild}s.
 */
public final class TrollBuildBuilder {

	static final int ITEMS_SIZE = 6;
	static final int SPELLS_SIZE = 2;

	private List<Item> items = new ArrayList<>();
	private int totalGold;
	private List<SummonerSpell> summonerSpells = new ArrayList<>();
	private Item trinket;

	private TrollBuildBuilder withBoots(Item boots) {
		if (boots != null) {
			items.add(0, boots);
		}
		return this;
	}

	public TrollBuildBuilder withBoots(Collection<Item> boots) {
		if (boots != null && !boots.isEmpty()) {
			return withBoots(RandomizeUtil.getRandom(boots));
		}
		return this;
	}

	public TrollBuildBuilder withItems(Collection<Item> items) {
		if (items != null && !items.isEmpty()) {
			this.items.addAll(RandomizeUtil.getRandoms(items, ITEMS_SIZE - 1)); // (without boots)
			this.totalGold += this.items.stream()
					.mapToInt(item -> item.getGold().getTotal())
					.sum();
		}
		return this;
	}

	public TrollBuildBuilder withSummonerSpells(List<SummonerSpell> summonerSpells) {
		if (summonerSpells != null && !summonerSpells.isEmpty()) {
			this.summonerSpells.addAll(RandomizeUtil.getRandoms(summonerSpells, SPELLS_SIZE));
		}
		return this;
	}

	private TrollBuildBuilder withTrinket(Item trinket) {
		if (trinket != null) {
			this.trinket = trinket;
			this.totalGold += this.trinket.getGold().getTotal();
		}
		return this;
	}

	public TrollBuildBuilder withTrinket(List<Item> trinkets) {
		if (trinkets != null && !trinkets.isEmpty()) {
			return withTrinket(RandomizeUtil.getRandom(trinkets));
		}
		return this;
	}

	public TrollBuildBuilder withViktor(List<Item> viktorOnly) {
		if (viktorOnly != null && !viktorOnly.isEmpty()) {
			items.remove(items.size() - 1);
			items.add(1, RandomizeUtil.getRandom(viktorOnly));
		}
		return this;
	}

	public TrollBuild build() {
		TrollBuild trollBuild = new TrollBuild();
		trollBuild.setItems(items);
		trollBuild.setTotalGold(totalGold);
		trollBuild.setSummonerSpells(summonerSpells);
		trollBuild.setTrinket(trinket);
		return trollBuild;
	}

}
