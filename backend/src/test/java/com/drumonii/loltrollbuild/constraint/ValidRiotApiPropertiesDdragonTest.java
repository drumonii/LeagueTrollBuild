package com.drumonii.loltrollbuild.constraint;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Ddragon;
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

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ValidRiotApiPropertiesDdragonTest {

	private LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

	private RiotApiProperties properties = new RiotApiProperties();

	private Ddragon ddragon = new Ddragon();

	@Before
	public void before() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setEnvironment(new MockEnvironment()
				.withProperty("spring.profiles.active", DDRAGON));
		context.refresh();

		validator.setApplicationContext(context);
		validator.setProviderClass(HibernateValidator.class);
		validator.afterPropertiesSet();

		properties.setDdragon(ddragon);
	}

	@Test
	public void nullDdragon() {
		properties.setDdragon(null);

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("riot.ddragon.* property namespace must " +
				"not be empty for Data Dragon");
	}

	@Test
	public void invalidBaseUrl() {
		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Base URL must not be empty for Data Dragon API");

		ddragon.setBaseUrl("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Base URL must not be empty for Data Dragon API");
	}

	@Test
	public void invalidLocale() {
		ddragon.setBaseUrl("base_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Locale must not be empty for Data Dragon API");

		ddragon.setLocale("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Locale must not be empty for Data Dragon API");
	}

	@Test
	public void invalidChampionsUrl() {
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champions URL must not be empty for Data Dragon API");

		ddragon.setChampions("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champions URL must not be empty for Data Dragon API");
	}

	@Test
	public void invalidChampionUrl() {
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");
		ddragon.setChampions("champions_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champion URL must not be empty for Data Dragon API");

		ddragon.setChampion("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Champion URL must not be empty for Data Dragon API");
	}

	@Test
	public void invalidItemsUrl() {
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");
		ddragon.setChampions("champions_url");
		ddragon.setChampion("champion_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Items URL must not be empty for Data Dragon API");

		ddragon.setItems("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Items URL must not be empty for Data Dragon API");
	}

	@Test
	public void invalidMapsUrl() {
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");
		ddragon.setChampions("champions_url");
		ddragon.setChampion("champion_url");
		ddragon.setItems("items_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Maps URL must not be empty for Data Dragon API");

		ddragon.setMaps("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Maps URL must not be empty for Data Dragon API");
	}

	@Test
	public void invalidSummonerSpellsUrl() {
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");
		ddragon.setChampions("champions_url");
		ddragon.setChampion("champion_url");
		ddragon.setItems("items_url");
		ddragon.setMaps("maps_url");

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Summoner Spells URL must not be empty for Data Dragon API");

		ddragon.setSummonerSpells("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Summoner Spells URL must not be empty for Data Dragon API");
	}

	@Test
	public void invalidChampionsImg() {
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");
		ddragon.setChampions("champions_url");
		ddragon.setChampion("champion_url");
		ddragon.setItems("items_url");
		ddragon.setMaps("maps_url");
		ddragon.setSummonerSpells("summoner_spells_url");

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
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");
		ddragon.setChampions("champions_url");
		ddragon.setChampion("champion_url");
		ddragon.setItems("items_url");
		ddragon.setMaps("maps_url");
		ddragon.setSummonerSpells("summoner_spells_url");
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
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");
		ddragon.setChampions("champions_url");
		ddragon.setChampion("champion_url");
		ddragon.setItems("items_url");
		ddragon.setMaps("maps_url");
		ddragon.setSummonerSpells("summoner_spells_url");
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
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");
		ddragon.setChampions("champions_url");
		ddragon.setChampion("champion_url");
		ddragon.setItems("items_url");
		ddragon.setMaps("maps_url");
		ddragon.setSummonerSpells("summoner_spells_url");
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
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");
		ddragon.setChampions("champions_url");
		ddragon.setChampion("champion_url");
		ddragon.setItems("items_url");
		ddragon.setMaps("maps_url");
		ddragon.setSummonerSpells("summoner_spells_url");
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
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");
		ddragon.setChampions("champions_url");
		ddragon.setChampion("champion_url");
		ddragon.setItems("items_url");
		ddragon.setMaps("maps_url");
		ddragon.setSummonerSpells("summoner_spells_url");
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
	public void validDdragon() {
		ddragon.setBaseUrl("base_url");
		ddragon.setLocale("locale");
		ddragon.setChampions("champions_url");
		ddragon.setChampion("champion_url");
		ddragon.setItems("items_url");
		ddragon.setMaps("maps_url");
		ddragon.setSummonerSpells("summoner_spells_url");
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
