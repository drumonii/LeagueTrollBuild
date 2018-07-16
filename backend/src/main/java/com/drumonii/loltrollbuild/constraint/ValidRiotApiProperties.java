package com.drumonii.loltrollbuild.constraint;

import com.drumonii.loltrollbuild.constraint.validator.ValidRiotApiPropertiesConstraintValidator;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Type constraint to validate the {@link RiotApiProperties} of the {@link RiotApiProperties}.
 */
@Constraint(validatedBy = { ValidRiotApiPropertiesConstraintValidator.class })
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ValidRiotApiProperties {

	String message() default "{Invalid RiotApiProperties}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
