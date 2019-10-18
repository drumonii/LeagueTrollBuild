package com.drumonii.loltrollbuild.constraint;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Ddragon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = false)
@ImportAutoConfiguration({ ValidationAutoConfiguration.class })
@ActiveProfiles({ TESTING, DDRAGON })
class ValidRiotApiPropertiesDdragonTest {

	@Autowired
	private LocalValidatorFactoryBean validator;

	private RiotApiProperties properties = new RiotApiProperties();

	private Ddragon ddragon = new Ddragon();

	@BeforeEach
	void beforeEach() {
		properties.setDdragon(ddragon);
	}

	@Test
	void nullDdragon() {
		properties.setDdragon(null);

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("riot.ddragon.* property namespace must " +
				"not be empty for Data Dragon");
	}

	@Test
	void invalidBaseUrl() {
		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Base URL must not be empty for Data Dragon API");

		ddragon.setBaseUrl("");

		result = validator.validate(properties);
		assertThat(result).hasSize(1);
		assertThat(result).extracting(ConstraintViolation::getMessage).containsOnly("Base URL must not be empty for Data Dragon API");
	}

	@Test
	void invalidLocale() {
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
	void invalidChampionsUrl() {
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
	void invalidChampionUrl() {
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
	void invalidItemsUrl() {
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
	void invalidMapsUrl() {
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
	void invalidSummonerSpellsUrl() {
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
	void invalidChampionsImg() {
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
	void invalidChampionsSpellImg() {
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
	void invalidChampionsPassiveImg() {
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
	void invalidItemsImg() {
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
	void invalidMapsImg() {
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
	void invalidSummonerSpellsImg() {
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
	void validDdragon() {
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
