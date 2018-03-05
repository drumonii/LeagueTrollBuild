package com.drumonii.loltrollbuild.rest.specification;

import com.drumonii.loltrollbuild.model.Item;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * {@link Specification} to build a {@link Item} {@link Predicate} from a QBE {@link Example}.
 */
public class ItemSpecification implements Specification<Item> {

	public Example<Item> example;

	public ItemSpecification(Example<Item> example) {
		this.example = example;
	}

	@Override
	public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		return QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);
	}

}