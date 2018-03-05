package com.drumonii.loltrollbuild.rest.specification;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * {@link Specification} to build a {@link SummonerSpell} {@link Predicate} from a QBE {@link Example}.
 */
public class SummonerSpellsSpecification implements Specification<SummonerSpell> {

	public Example<SummonerSpell> example;

	public SummonerSpellsSpecification(Example<SummonerSpell> example) {
		this.example = example;
	}

	@Override
	public Predicate toPredicate(Root<SummonerSpell> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		return QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);
	}

}