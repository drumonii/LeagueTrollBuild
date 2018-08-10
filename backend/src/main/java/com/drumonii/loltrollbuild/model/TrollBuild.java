package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.rest.view.ApiViews;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

/**
 * Troll Build (random build).
 */
public class TrollBuild {

	@JsonProperty
	@JsonView(ApiViews.LtbApi.class)
	private List<Item> items;

	@JsonProperty
	@JsonView(ApiViews.LtbApi.class)
	private int totalGold;

	@JsonProperty
	@JsonView(ApiViews.LtbApi.class)
	private List<SummonerSpell> summonerSpells;

	@JsonProperty
	@JsonView(ApiViews.LtbApi.class)
	private Item trinket;

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public int getTotalGold() {
		return totalGold;
	}

	public void setTotalGold(int totalGold) {
		this.totalGold = totalGold;
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
