package com.drumonii.loltrollbuild.constraint;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
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

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = false)
@ImportAutoConfiguration({ ValidationAutoConfiguration.class })
@ActiveProfiles({ TESTING })
class ValidRiotApiPropertiesTest {

	@Autowired
	private LocalValidatorFactoryBean validator;

	private RiotApiProperties properties = new RiotApiProperties();

	@Test
	void invalidProfile() {
		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).isNotEmpty();
	}

}
