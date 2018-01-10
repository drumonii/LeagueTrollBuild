package com.drumonii.loltrollbuild.constraint;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Ddragon;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.StaticData;
import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ValidRiotApiPropertiesStaticDataTest {

	private LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

	private RiotApiProperties properties = new RiotApiProperties();

	private StaticData staticData = new StaticData();
	private Ddragon ddragon = new Ddragon();

	@Before
	public void before() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setEnvironment(new MockEnvironment()
				.withProperty("spring.profiles.active", STATIC_DATA));
		context.refresh();

		validator.setApplicationContext(context);
		validator.setProviderClass(HibernateValidator.class);
		validator.afterPropertiesSet();

		staticData.setApiKey("API_KEY");
		properties.setStaticData(staticData);
		properties.setDdragon(ddragon);
	}

	@Test
	public void nullStaticData() {
		properties.setStaticData(null);

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("riot.static-data.* property namespace must " +
				"not be empty for static data API");
	}

	@Test
	public void invalidApiKey() {
		staticData.setApiKey(null);

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("API key must not be empty for static data API");

		staticData.setApiKey("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("API key must not be empty for static data API");
	}

	@Test
	public void invalidBaseUrl() {
		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Base URL must not be empty for static data API");

		staticData.setBaseUrl("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Base URL must not be empty for static data API");
	}

	@Test
	public void invalidKeyParam() {
		staticData.setBaseUrl("base_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Key param must not be empty for static data API");

		staticData.setKeyParam("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Key param must not be empty for static data API");
	}

	@Test
	public void invalidLocaleParam() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Locale param must not be empty for static data API");

		staticData.setLocaleParam("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Locale param must not be empty for static data API");
	}

	@Test
	public void invalidLocale() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Locale must not be empty for static data API");

		staticData.setLocale("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Locale must not be empty for static data API");
	}

	@Test
	public void invalidRegion() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Region must not be empty for static data API");

		staticData.setRegion("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Region must not be empty for static data API");
	}

	@Test
	public void invalidChampionsUrl() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champions URL must not be empty for static data API");

		staticData.setChampions("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champions URL must not be empty for static data API");
	}

	@Test
	public void invalidChampionUrl() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champion URL must not be empty for static data API");

		staticData.setChampion("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champion URL must not be empty for static data API");
	}

	@Test
	public void invalidItemsUrl() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Items URL must not be empty for static data API");

		staticData.setItems("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Items URL must not be empty for static data API");
	}

	@Test
	public void invalidItemUrl() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Item URL must not be empty for static data API");

		staticData.setItem("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Item URL must not be empty for static data API");
	}

	@Test
	public void invalidMapsUrl() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Maps URL must not be empty for static data API");

		staticData.setMaps("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Maps URL must not be empty for static data API");
	}

	@Test
	public void invalidSummonerSpellsUrl() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");
		staticData.setMaps("maps_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Summoner Spells URL must not be empty for static data API");

		staticData.setSummonerSpells("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Summoner Spells URL must not be empty for static data API");
	}

	@Test
	public void invalidSummonerSpellUrl() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");
		staticData.setMaps("maps_url");
		staticData.setSummonerSpells("summoner_spells_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Summoner Spell URL must not be empty for static data API");

		staticData.setSummonerSpell("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Summoner Spell URL must not be empty for static data API");
	}

	@Test
	public void invalidVersionsUrl() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");
		staticData.setMaps("maps_url");
		staticData.setSummonerSpells("summoner_spells_url");
		staticData.setSummonerSpell("summoner_spell_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Versions URL must not be empty for static data API");

		staticData.setVersions("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Versions URL must not be empty for static data API");
	}

	@Test
	public void nullDdragon() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");
		staticData.setMaps("maps_url");
		staticData.setSummonerSpells("summoner_spells_url");
		staticData.setSummonerSpell("summoner_spell_url");
		staticData.setVersions("versions_url");

		properties.setDdragon(null);

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("riot.api.ddragon.* property namespace must " +
				"not be empty for Data Dragon Images");
	}

	@Test
	public void invalidChampionsImg() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");
		staticData.setMaps("maps_url");
		staticData.setSummonerSpells("summoner_spells_url");
		staticData.setSummonerSpell("summoner_spell_url");
		staticData.setVersions("versions_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champions Image URL must not be empty");

		ddragon.setChampionsImg("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champions Image URL must not be empty");
	}

	@Test
	public void invalidChampionsSpellImg() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");
		staticData.setMaps("maps_url");
		staticData.setSummonerSpells("summoner_spells_url");
		staticData.setSummonerSpell("summoner_spell_url");
		staticData.setVersions("versions_url");
		ddragon.setChampionsImg("champions_img_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champions Spell Image URL must not be empty");

		ddragon.setChampionsSpellImg("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champions Spell Image URL must not be empty");
	}

	@Test
	public void invalidChampionsPassiveImg() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");
		staticData.setMaps("maps_url");
		staticData.setSummonerSpells("summoner_spells_url");
		staticData.setSummonerSpell("summoner_spell_url");
		staticData.setVersions("versions_url");
		ddragon.setChampionsImg("champions_img_url");
		ddragon.setChampionsSpellImg("champions_spell_img_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champions Passive Image URL must not be empty");

		ddragon.setChampionsPassiveImg("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champions Passive Image URL must not be empty");
	}

	@Test
	public void invalidItemsImg() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");
		staticData.setMaps("maps_url");
		staticData.setSummonerSpells("summoner_spells_url");
		staticData.setSummonerSpell("summoner_spell_url");
		staticData.setVersions("versions_url");
		ddragon.setChampionsImg("champions_img_url");
		ddragon.setChampionsSpellImg("champions_spell_img_url");
		ddragon.setChampionsPassiveImg("champions_passive_img_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Items Image URL must not be empty");

		ddragon.setItemsImg("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Items Image URL must not be empty");
	}

	@Test
	public void invalidMapsImg() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");
		staticData.setMaps("maps_url");
		staticData.setSummonerSpells("summoner_spells_url");
		staticData.setSummonerSpell("summoner_spell_url");
		staticData.setVersions("versions_url");
		ddragon.setChampionsImg("champions_img_url");
		ddragon.setChampionsSpellImg("champions_spell_img_url");
		ddragon.setChampionsPassiveImg("champions_passive_img_url");
		ddragon.setItemsImg("items_img_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Maps Image URL must not be empty");

		ddragon.setMapsImg("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Maps Image URL must not be empty");
	}

	@Test
	public void invalidSummonerSpellsImg() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");
		staticData.setMaps("maps_url");
		staticData.setSummonerSpells("summoner_spells_url");
		staticData.setSummonerSpell("summoner_spell_url");
		staticData.setVersions("versions_url");
		ddragon.setChampionsImg("champions_img_url");
		ddragon.setChampionsSpellImg("champions_spell_img_url");
		ddragon.setChampionsPassiveImg("champions_passive_img_url");
		ddragon.setItemsImg("items_img_url");
		ddragon.setMapsImg("maps_img_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Summoner Spells Image URL must not be empty");

		ddragon.setSummonerSpellsImg("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Summoner Spells Image URL must not be empty");
	}

	@Test
	public void validStaticData() {
		staticData.setBaseUrl("base_url");
		staticData.setKeyParam("key_param");
		staticData.setLocaleParam("locale_param");
		staticData.setLocale("locale");
		staticData.setRegion("region");
		staticData.setChampions("champions_url");
		staticData.setChampion("champion_url");
		staticData.setItems("items_url");
		staticData.setItem("item_url");
		staticData.setMaps("maps_url");
		staticData.setSummonerSpells("summoner_spells_url");
		staticData.setSummonerSpell("summoner_spell_url");
		staticData.setVersions("versions_url");
		ddragon.setChampionsImg("champions_img_url");
		ddragon.setChampionsSpellImg("champions_spell_img_url");
		ddragon.setChampionsPassiveImg("champions_passive_img_url");
		ddragon.setItemsImg("items_img_url");
		ddragon.setMapsImg("maps_img_url");
		ddragon.setSummonerSpellsImg("summoner_spells_img_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).isEmpty();
	}

}
