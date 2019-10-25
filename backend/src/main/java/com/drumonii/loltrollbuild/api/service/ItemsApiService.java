package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * API {@code @Service} for {@link Item}s.
 */
public interface ItemsApiService {

    /**
     * Queries by example from the given {@link ExampleSpecification} of {@link Item}.
     *
     * @param specification the QBE {@link ExampleSpecification}
     * @param sort the {@link Sort}
     * @return the {@link List} of {@link Item}
     */
    List<Item> qbe(ExampleSpecification<Item> specification, Sort sort);

    /**
     * Finds the {@link Item} using the given item id.
     *
     * @param id the item id
     * @return the {@link Optional} {@link Item}
     */
    Optional<Item> findById(int id);

    /**
     * Gets all {@link Item}s eligible for the troll build of the given map id.
     *
     * @param mapId the map id
     * @return the {@link List} of {@link Item}s
     */
    List<Item> forTrollBuild(int mapId);

    /**
     * Gets all boots {@link Item}s for the given map id.
     *
     * @param mapId the map id
     * @return the {@link List} of {@link Item}s
     */
    List<Item> boots(int mapId);

    /**
     * Gets all Trinket {@link Item}s for the given map id.
     *
     * @param mapId the map id
     * @return the {@link List} of {@link Item}s
     */
    List<Item> trinkets(int mapId);

    /**
     * Gets all {@link Item}s that only Viktor starts with.
     *
     * @return the {@link List} of {@link Item}s
     */
    List<Item> viktorOnly();

}
