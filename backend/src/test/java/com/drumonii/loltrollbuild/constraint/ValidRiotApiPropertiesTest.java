package com.drumonii.loltrollbuild.constraint;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import org.hibernate.validator.HibernateValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ValidRiotApiPropertiesTest {

	private AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

	private LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

	private RiotApiProperties properties = new RiotApiProperties();

	@Test
	public void invalidProfile() {
		context.refresh();

		validator.setApplicationContext(context);
		validator.setProviderClass(HibernateValidator.class);
		validator.afterPropertiesSet();

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).isNotEmpty();
	}

	@Test
	public void validTestingProfile() {
		context.setEnvironment(new MockEnvironment()
				.withProperty("spring.profiles.active", TESTING));
		context.refresh();

		validator.setApplicationContext(context);
		validator.setProviderClass(HibernateValidator.class);
		validator.afterPropertiesSet();

		Set<ConstraintViolation<RiotApiProperties>> result = validator.validate(properties);
		assertThat(result).isEmpty();
	}

}
