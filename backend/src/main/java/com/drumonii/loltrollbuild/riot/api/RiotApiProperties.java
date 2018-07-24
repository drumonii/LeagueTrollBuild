package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.constraint.ValidRiotApiProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for Riot's API found in config/application.yml of the resources folder.
 */
@Validated
@ValidRiotApiProperties
@ConfigurationProperties(prefix = "riot")
public class RiotApiProperties {

	@NestedConfigurationProperty
	private Ddragon ddragon;

	public Ddragon getDdragon() {
		return ddragon;
	}

	public void setDdragon(Ddragon ddragon) {
		this.ddragon = ddragon;
	}

	/**
	 * Configuration for Riot's {@code Data Dragon}, a web service that is meant to centralize League of Legends game
	 * data.
	 */
	public static class Ddragon {

		private String baseUrl;
		private String locale;
		private String champions;
		private String champion;
		private String championsImg;
		private String championsSpellImg;
		private String championsPassiveImg;
		private String items;
		private String itemsImg;
		private String maps;
		private String mapsImg;
		private String summonerSpells;
		private String summonerSpellsImg;
		private String versions;

		public String getBaseUrl() {
			return baseUrl;
		}

		public void setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
		}

		public String getLocale() {
			return locale;
		}

		public void setLocale(String locale) {
			this.locale = locale;
		}

		public String getChampions() {
			return champions;
		}

		public void setChampions(String champions) {
			this.champions = champions;
		}

		public String getChampion() {
			return champion;
		}

		public void setChampion(String champion) {
			this.champion = champion;
		}

		public String getChampionsImg() {
			return championsImg;
		}

		public void setChampionsImg(String championsImg) {
			this.championsImg = championsImg;
		}

		public String getChampionsSpellImg() {
			return championsSpellImg;
		}

		public void setChampionsSpellImg(String championsSpellImg) {
			this.championsSpellImg = championsSpellImg;
		}

		public String getChampionsPassiveImg() {
			return championsPassiveImg;
		}

		public void setChampionsPassiveImg(String championsPassiveImg) {
			this.championsPassiveImg = championsPassiveImg;
		}

		public String getItems() {
			return items;
		}

		public void setItems(String items) {
			this.items = items;
		}

		public String getItemsImg() {
			return itemsImg;
		}

		public void setItemsImg(String itemsImg) {
			this.itemsImg = itemsImg;
		}

		public String getMaps() {
			return maps;
		}

		public void setMaps(String maps) {
			this.maps = maps;
		}

		public String getMapsImg() {
			return mapsImg;
		}

		public void setMapsImg(String mapsImg) {
			this.mapsImg = mapsImg;
		}

		public String getSummonerSpells() {
			return summonerSpells;
		}

		public void setSummonerSpells(String summonerSpells) {
			this.summonerSpells = summonerSpells;
		}

		public String getSummonerSpellsImg() {
			return summonerSpellsImg;
		}

		public void setSummonerSpellsImg(String summonerSpellsImg) {
			this.summonerSpellsImg = summonerSpellsImg;
		}

		public String getVersions() {
			return versions;
		}

		public void setVersions(String versions) {
			this.versions = versions;
		}

	}

}
