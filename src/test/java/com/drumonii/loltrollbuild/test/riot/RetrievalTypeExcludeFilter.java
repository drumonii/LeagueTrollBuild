package com.drumonii.loltrollbuild.test.riot;

import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.test.autoconfigure.filter.AnnotationCustomizableTypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * {@link TypeExcludeFilter} for {@link RetrievalTest @RetrievalTest}.
 */
public class RetrievalTypeExcludeFilter extends AnnotationCustomizableTypeExcludeFilter {

	private static final Set<Class<?>> DEFAULT_INCLUDES;

	static {
		Set<Class<?>> includes = new LinkedHashSet<>();
		includes.add(JsonComponent.class);
		DEFAULT_INCLUDES = Collections.unmodifiableSet(includes);
	}

	private final RetrievalTest annotation;

	RetrievalTypeExcludeFilter(Class<?> testClass) {
		annotation = AnnotatedElementUtils.getMergedAnnotation(testClass, RetrievalTest.class);
	}

	@Override
	protected boolean hasAnnotation() {
		return annotation != null;
	}

	@Override
	protected Filter[] getFilters(FilterType type) {
		switch (type) {
		case INCLUDE:
			return annotation.includeFilters();
		case EXCLUDE:
			return annotation.excludeFilters();
		}
		throw new IllegalStateException("Unsupported type " + type);
	}

	@Override
	protected boolean isUseDefaultFilters() {
		return true;
	}

	@Override
	protected Set<Class<?>> getDefaultIncludes() {
		return DEFAULT_INCLUDES;
	}

	@Override
	protected Set<Class<?>> getComponentIncludes() {
		return new LinkedHashSet<>(Arrays.asList(annotation.retrievals()));
	}

}
