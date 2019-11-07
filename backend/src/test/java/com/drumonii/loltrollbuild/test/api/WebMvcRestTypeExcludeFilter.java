package com.drumonii.loltrollbuild.test.api;

import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.test.autoconfigure.filter.StandardAnnotationCustomizableTypeExcludeFilter;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * {@link TypeExcludeFilter} for {@link WebMvcRestTest @WebMvcRestTest}.
 */
public class WebMvcRestTypeExcludeFilter extends StandardAnnotationCustomizableTypeExcludeFilter<WebMvcRestTest> {

	private static final Set<Class<?>> DEFAULT_INCLUDES;

	static {
		Set<Class<?>> includes = new LinkedHashSet<>();
		includes.add(JsonComponent.class);
		includes.add(Service.class);
		DEFAULT_INCLUDES = Collections.unmodifiableSet(includes);
	}

	WebMvcRestTypeExcludeFilter(Class<?> testClass) {
		super(testClass);
	}

	@Override
	protected Set<Class<?>> getDefaultIncludes() {
		return DEFAULT_INCLUDES;
	}

	@Override
	protected Set<Class<?>> getComponentIncludes() {
		return new LinkedHashSet<>(Arrays.asList(getAnnotation().synthesize().controllers()));
	}

}
