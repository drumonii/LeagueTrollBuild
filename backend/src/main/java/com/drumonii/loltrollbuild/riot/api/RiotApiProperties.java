package com.drumonii.loltrollbuild.riot.api;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * Configuration properties for Riot's API found in config/application.yml of the resources folder.
 */
@Validated
@ConfigurationProperties(prefix = "riot")
public class RiotApiProperties {

	@NestedConfigurationProperty
	@Valid
	private Ddragon ddragon;

	public Ddragon getDdragon() {
		return ddragon;
	}

	public void setDdragon(Ddragon ddragon) {
		this.ddragon = ddragon;
	}

	/**
	 * Configuration for Riot's Data Dragon, a web service that is meant to centralize League of Legends game data.
	 */
	public static class Ddragon {

		@NotEmpty
		@URL
		private String baseUrl;

		@NotNull
		private Locale locale;

		@NotEmpty
		private String champions;

		@NotEmpty
		private String champion;

		@NotEmpty
		private String championsImg;

		@NotEmpty
		private String championsSpellImg;

		@NotEmpty
		private String championsPassiveImg;

		@NotEmpty
		private String items;

		@NotEmpty
		private String itemsImg;

		@NotEmpty
		private String maps;

		@NotEmpty
		private String mapsImg;

		@NotEmpty
		private String summonerSpells;

		@NotEmpty
		private String summonerSpellsImg;

		@NotEmpty
		private String versions;

		public String getBaseUrl() {
			return baseUrl;
		}

		public void setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
		}

		public Locale getLocale() {
			return locale;
		}

		public void setLocale(Locale locale) {
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
