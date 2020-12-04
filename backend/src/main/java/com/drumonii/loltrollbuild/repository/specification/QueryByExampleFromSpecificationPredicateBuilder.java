package com.drumonii.loltrollbuild.repository.specification;

import org.springframework.beans.BeanWrapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.NullHandler;
import org.springframework.data.domain.ExampleMatcher.PropertyValueTransformer;
import org.springframework.data.support.ExampleMatcherAccessor;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.*;
import java.util.stream.Stream;

/**
 * Creates a single {@link Predicate} for a given {@link Example}.
 */
public class QueryByExampleFromSpecificationPredicateBuilder {

	private QueryByExampleFromSpecificationPredicateBuilder() {}

	/**
	 * Extract the {@link Predicate} representing the {@link Example}.
	 *
	 * @param root the {@link Root}
	 * @param cb the {@link CriteriaBuilder}
	 * @param example the {@link Example}
	 * @param <T> the type from which the {@link Predicate} will be built upon
	 * @return the {@link Predicate}
	 */
	public static <T> Predicate getPredicate(Root<T> root, CriteriaBuilder cb, Example<T> example) {
		BeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(example.getProbe());

		List<Predicate> singleAttributePredicates = getSingleAttributePredicates(cb, root, root.getModel(),
				beanWrapper, new ExampleMatcherAccessor(example.getMatcher()));

		List<Predicate> pluralAttributePredicates = getPluralAttributePredicates(cb, root, root.getModel(),
				beanWrapper, new ExampleMatcherAccessor(example.getMatcher()));

		if (singleAttributePredicates.isEmpty() && pluralAttributePredicates.isEmpty()) {
			return cb.isTrue(cb.literal(true));
		}

		return cb.and(Stream.concat(singleAttributePredicates.stream(), pluralAttributePredicates.stream())
				.toArray(Predicate[]::new));
	}

	/**
	 * Gets a {@link List} of {@link Predicate}s based on the {@link EntityType}'s single attributes and the
	 * {@link ExampleMatcherAccessor} provided by the {@link Example}'s {@link ExampleMatcher}.
	 *
	 * @param cb the {@link CriteriaBuilder}
	 * @param root the {@link Root}
	 * @param model the {@link EntityType} to get {@link Attribute}s.
	 * @param beanWrapper the value of the example
	 * @param exampleAccessor the {@link ExampleMatcherAccessor}
	 * @param <T> the type of the example
	 * @return the {@link List} of {@link Predicate}s.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static <T> List<Predicate> getSingleAttributePredicates(CriteriaBuilder cb, Root<T> root, EntityType<T> model,
			BeanWrapper beanWrapper, ExampleMatcherAccessor exampleAccessor) {
		List<Predicate> predicates = new ArrayList<>();

		for (SingularAttribute attribute : model.getSingularAttributes()) {
			String currentPath = attribute.getName();

			if (exampleAccessor.isIgnoredPath(currentPath)) {
				continue;
			}
			PropertyValueTransformer transformer = exampleAccessor.getValueTransformerForPath(currentPath);

			Optional<Object> optionalValue = transformer
					.apply(Optional.ofNullable(beanWrapper.getPropertyValue(attribute.getName())));

			if (optionalValue.isEmpty()) {

				if (exampleAccessor.getNullHandler().equals(NullHandler.INCLUDE)) {
					predicates.add(cb.isNull(root.get(attribute)));
				}
				continue;
			}

			Object attributeValue = optionalValue.get();

			if (attribute.getJavaType().equals(String.class)) {

				Expression<String> expression = root.get(attribute);
				if (exampleAccessor.isIgnoreCaseForPath(currentPath)) {
					expression = cb.lower(expression);
					attributeValue = attributeValue.toString().toLowerCase();
				}

				switch (exampleAccessor.getStringMatcherForPath(currentPath)) {
				case DEFAULT:
				case EXACT:
					predicates.add(cb.equal(expression, attributeValue));
					break;
				case CONTAINING:
					predicates.add(cb.like(expression, "%" + attributeValue + "%"));
					break;
				case STARTING:
					predicates.add(cb.like(expression, attributeValue + "%"));
					break;
				case ENDING:
					predicates.add(cb.like(expression, "%" + attributeValue));
					break;
				default:
					break;
				}
			} else {
				predicates.add(cb.equal(root.get(attribute), attributeValue));
			}
		}
		return predicates;
	}

	/**
	 * Gets a {@link List} of {@link Predicate}s based on the {@link EntityType}'s plural attributes and the
	 * {@link ExampleMatcherAccessor} provided by the {@link Example}'s {@link ExampleMatcher}. Note, like statements
	 * are not supported on Collection values.
	 *
	 * @param cb the {@link CriteriaBuilder}
	 * @param root the {@link Root}
	 * @param model the {@link EntityType} to get {@link Attribute}s.
	 * @param beanWrapper the value of the example
	 * @param exampleAccessor the {@link ExampleMatcherAccessor}
	 * @param <T> the type of the example
	 * @return the {@link List} of {@link Predicate}s.
	 */
	static <T> List<Predicate> getPluralAttributePredicates(CriteriaBuilder cb, Root<T> root, EntityType<T> model,
			BeanWrapper beanWrapper, ExampleMatcherAccessor exampleAccessor) {
		List<Predicate> predicates = new ArrayList<>();

		for (PluralAttribute attribute : model.getDeclaredPluralAttributes()) {
			String currentPath = attribute.getName();

			if (exampleAccessor.isIgnoredPath(currentPath)) {
				continue;
			}

			if (attribute.getPersistentAttributeType() == PersistentAttributeType.ELEMENT_COLLECTION) {
				PropertyValueTransformer transformer = exampleAccessor.getValueTransformerForPath(currentPath);
				Optional<Object> optionalValues = transformer
						.apply(Optional.ofNullable(beanWrapper.getPropertyValue(attribute.getName())));
				if (optionalValues.isPresent()) {
					Object attributeValues = optionalValues.get();

					if (Collection.class.isAssignableFrom(attribute.getJavaType())) {
						for (Object attributeValue : (Collection<Object>) attributeValues) {
							predicates.add(cb.isMember(attributeValue, root.get(attribute)));
						}
					} else if (Map.class.isAssignableFrom(attribute.getJavaType())) {
						Set<Map.Entry<Object, Object>> entrySet = ((Map) attributeValues).entrySet();
						MapJoin<T, Object, Object> mapJoin = root.joinMap(currentPath);
						for (Map.Entry<Object, Object> entry : entrySet) {
							predicates.add(cb.equal(mapJoin.key(), entry.getKey()));
							predicates.add(cb.equal(mapJoin.value(), entry.getValue()));
						}
					}
				}
			}
		}
		return predicates;
	}

}
