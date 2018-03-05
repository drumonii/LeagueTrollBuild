package com.drumonii.loltrollbuild.rest.specification;

import com.drumonii.loltrollbuild.model.GameMap;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * {@link Specification} to build a {@link GameMap} {@link Predicate} from a QBE {@link Example}.
 */
public class MapsSpecification implements Specification<GameMap> {

	public Example<GameMap> example;

	public MapsSpecification(Example<GameMap> example) {
		this.example = example;
	}

	@Override
	public Predicate toPredicate(Root<GameMap> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		return QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);
	}

}
