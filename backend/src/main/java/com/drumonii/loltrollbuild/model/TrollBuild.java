package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Troll Build (random build).
 */
public class TrollBuild {

	@JsonProperty
	private List<Item> items;

	@JsonProperty
	private List<SummonerSpell> summonerSpells;

	@JsonProperty
	private Item trinket;

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<SummonerSpell> getSummonerSpells() {
		return summonerSpells;
	}

	public void setSummonerSpells(List<SummonerSpell> summonerSpells) {
		this.summonerSpells = summonerSpells;
	}

	public Item getTrinket() {
		return trinket;
	}

	public void setTrinket(Item trinket) {
		this.trinket = trinket;
	}

}
