package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * API {@code @Service} for {@link GameMap}s.
 */
public interface MapsApiService {

    /**
     * Queries by example from the given {@link ExampleSpecification} of {@link GameMap}.
     *
     * @param specification the QBE {@link ExampleSpecification}
     * @param sort the {@link Sort}
     * @return the {@link List} of {@link GameMap}
     */
    List<GameMap> qbe(ExampleSpecification<GameMap> specification, Sort sort);

    /**
     * Finds the {@link GameMap} using the given map id.
     *
     * @param mapId the map id
     * @return the {@link Optional} {@link GameMap}
     */
    Optional<GameMap> findById(int mapId);

    /**
     * Gets all {@link GameMap}s eligible for a troll build.
     *
     * @return the {@link List} of {@link GameMap}s
     */
    List<GameMap> forTrollBuild();

}
