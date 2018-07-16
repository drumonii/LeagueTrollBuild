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
	private StaticData staticData;

	@NestedConfigurationProperty
	private Ddragon ddragon;

	public StaticData getStaticData() {
		return staticData;
	}

	public void setStaticData(StaticData staticData) {
		this.staticData = staticData;
	}

	public Ddragon getDdragon() {
		return ddragon;
	}

	public void setDdragon(Ddragon ddragon) {
		this.ddragon = ddragon;
	}

	/**
	 * Query configuration for Riot's {@code lol-static-data-v3} API part of Riot's full API.
	 */
	public static class StaticData {

		private String apiKey;
		private String baseUrl;
		private String keyParam;
		private String localeParam;
		private String locale;
		private String region;
		private String champions;
		private String champion;
		private String items;
		private String item;
		private String maps;
		private String summonerSpells;
		private String summonerSpell;
		private String versions;

		public String getApiKey() {
			return apiKey;
		}

		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}

		public String getBaseUrl() {
			return baseUrl;
		}

		public void setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
		}

		public String getKeyParam() {
			return keyParam;
		}

		public void setKeyParam(String keyParam) {
			this.keyParam = keyParam;
		}

		public String getLocaleParam() {
			return localeParam;
		}

		public void setLocaleParam(String localeParam) {
			this.localeParam = localeParam;
		}

		public String getLocale() {
			return locale;
		}

		public void setLocale(String locale) {
			this.locale = locale;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
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

		public String getItems() {
			return items;
		}

		public void setItems(String items) {
			this.items = items;
		}

		public String getItem() {
			return item;
		}

		public void setItem(String item) {
			this.item = item;
		}

		public String getMaps() {
			return maps;
		}

		public void setMaps(String maps) {
			this.maps = maps;
		}

		public String getSummonerSpells() {
			return summonerSpells;
		}

		public void setSummonerSpells(String summonerSpells) {
			this.summonerSpells = summonerSpells;
		}

		public String getSummonerSpell() {
			return summonerSpell;
		}

		public void setSummonerSpell(String summonerSpell) {
			this.summonerSpell = summonerSpell;
		}

		public String getVersions() {
			return versions;
		}

		public void setVersions(String versions) {
			this.versions = versions;
		}

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
