package com.drumonii.loltrollbuild.test.api;

import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.test.autoconfigure.filter.AnnotationCustomizableTypeExcludeFilter;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * {@link TypeExcludeFilter} for {@link WebMvcRestTest @WebMvcRestTest}.
 */
public class WebMvcRestTypeExcludeFilter extends AnnotationCustomizableTypeExcludeFilter {

	private static final Set<Class<?>> DEFAULT_INCLUDES;

	static {
		Set<Class<?>> includes = new LinkedHashSet<>();
		includes.add(JsonComponent.class);
		includes.add(Service.class);
		// Not sure all this is needed but have it here anyway like WebMvcTypeExcludeFilter's
		includes.add(WebMvcConfigurer.class);
		includes.add(javax.servlet.Filter.class);
		includes.add(FilterRegistrationBean.class);
		includes.add(DelegatingFilterProxyRegistrationBean.class);
		includes.add(HandlerMethodArgumentResolver.class);
		includes.add(HttpMessageConverter.class);
		includes.add(ErrorAttributes.class);
		DEFAULT_INCLUDES = Collections.unmodifiableSet(includes);
	}

	private final WebMvcRestTest annotation;

	WebMvcRestTypeExcludeFilter(Class<?> testClass) {
		annotation = AnnotatedElementUtils.getMergedAnnotation(testClass, WebMvcRestTest.class);
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
		return new LinkedHashSet<>(Arrays.asList(annotation.controllers()));
	}

}
