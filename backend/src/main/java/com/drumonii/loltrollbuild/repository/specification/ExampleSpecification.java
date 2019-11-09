package com.drumonii.loltrollbuild.repository.specification;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * {@link Specification} to build a {@link Predicate} from a QBE {@link Example} of {@code <T>}.
 *
 * @param <T> the Example and Specification type
 */
public class ExampleSpecification<T> implements Specification<T> {

	private final Example<T> example;

	public ExampleSpecification(Example<T> example) {
		this.example = example;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		return QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);
	}

	public Example<T> getExample() {
		return example;
	}

}
