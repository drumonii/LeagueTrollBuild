package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * API {@code @Service} for {@link SummonerSpell}s.
 */
public interface SummonerSpellsApiService {

    /**
     * Queries by example from the given {@link ExampleSpecification} of {@link SummonerSpell}.
     *
     * @param specification the QBE {@link ExampleSpecification}
     * @param sort the {@link Sort}
     * @return the {@link List} of {@link SummonerSpell}
     */
    List<SummonerSpell> qbe(ExampleSpecification<SummonerSpell> specification, Sort sort);

    /**
     * Gets all {@link SummonerSpell}s eligible for the troll build of the given {@link GameMode}.
     *
     * @param mode the {@link GameMode} to limit
     * @return the {@link List} of {@link SummonerSpell}s
     */
    List<SummonerSpell> forTrollBuild(GameMode mode);

    /**
     * Finds the {@link SummonerSpell} using the given summoner spell id.
     *
     * @param id the summoner spell id
     * @return the {@link Optional} {@link SummonerSpell}
     */
    Optional<SummonerSpell> findById(int id);

}
