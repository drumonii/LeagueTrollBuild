package com.drumonii.loltrollbuild.constraint.validator;

import com.drumonii.loltrollbuild.constraint.ValidRiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Ddragon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

public class ValidRiotApiPropertiesConstraintValidator implements ConstraintValidator<ValidRiotApiProperties, RiotApiProperties> {

	@Autowired
	private Environment env;

	@Override
	public void initialize(ValidRiotApiProperties constraintAnnotation) {
		// Nothing to initialize
	}

	@Override
	public boolean isValid(RiotApiProperties properties, ConstraintValidatorContext context) {
		List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if (activeProfiles.contains(DDRAGON)) {
			return validate(properties.getDdragon(), context) && validateImgs(properties.getDdragon(), context);
		} else if (activeProfiles.contains(TESTING) && !activeProfiles.contains(DDRAGON)) {
			return true; // exception case if only testing profile is active
		}
		return false; // didn't have an active api profile
	}

	/**
	 * Validates the {@link Ddragon}.
	 *
	 * @param ddragon the {@link Ddragon} to validate
	 * @param context the {@link ConstraintValidatorContext}
	 * @return {@code true} whether valid, otherwise {@code false}
	 */
	private boolean validate(Ddragon ddragon, ConstraintValidatorContext context) {
		if (ddragon == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("riot.ddragon.* property namespace must " +
					"not be empty for Data Dragon")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getBaseUrl())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Base URL must not be empty for Data Dragon API")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getLocale())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Locale must not be empty for Data Dragon API")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getChampions())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Champions URL must not be empty for Data Dragon API")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getChampion())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Champion URL must not be empty for Data Dragon API")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getItems())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Items URL must not be empty for Data Dragon API")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getMaps())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Maps URL must not be empty for Data Dragon API")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getSummonerSpells())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Summoner Spells URL must not be empty for Data Dragon API")
					.addConstraintViolation();
			return false;
		}
		return true;
	}

	/**
	 * Validates the {@link Ddragon} images.
	 *
	 * @param ddragon the {@link Ddragon} to validate
	 * @param context the {@link ConstraintValidatorContext}
	 * @return {@code true} whether valid, otherwise {@code false}
	 */
	private boolean validateImgs(Ddragon ddragon, ConstraintValidatorContext context) {
		if (ddragon == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("riot.api.ddragon.* property namespace must " +
					"not be empty for Data Dragon Images")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getChampionsImg())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Champions Image URL must not be empty")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getChampionsSpellImg())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Champions Spell Image URL must not be empty")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getChampionsPassiveImg())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Champions Passive Image URL must not be empty")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getItemsImg())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Items Image URL must not be empty")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getMapsImg())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Maps Image URL must not be empty")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isEmpty(ddragon.getSummonerSpellsImg())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Summoner Spells Image URL must not be empty")
					.addConstraintViolation();
			return false;
		}
		return true;
	}

}
