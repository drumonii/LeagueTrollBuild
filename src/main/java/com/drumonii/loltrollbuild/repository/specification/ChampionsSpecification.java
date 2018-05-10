package com.drumonii.loltrollbuild.repository.specification;

import com.drumonii.loltrollbuild.model.Champion;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * {@link Specification} to build a {@link Champion} {@link Predicate} from a QBE {@link Example}.
 */
public class ChampionsSpecification implements Specification<Champion> {

	public Example<Champion> example;

	public ChampionsSpecification(Example<Champion> example) {
		this.example = example;
	}

	@Override
	public Predicate toPredicate(Root<Champion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		return QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);
	}

}