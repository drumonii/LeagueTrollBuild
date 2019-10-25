package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * API {@code @Service} for {@link Champion}s.
 */
public interface ChampionsApiService {

    /**
     * Queries by example from the given {@link ExampleSpecification} of {@link Champion}.
     *
     * @param specification the QBE {@link ExampleSpecification}
     * @param sort the {@link Sort}
     * @return the {@link List} of {@link Champion}
     */
    List<Champion> qbe(ExampleSpecification<Champion> specification, Sort sort);

    /**
     * Finds the {@link Champion} with the given value, either its id or name.
     *
     * @param value the value
     * @return the {@link Optional} {@link Champion}
     */
    Optional<Champion> find(String value);

    /**
     * Gets the Champion tags.
     *
     * @return the {@link List} of tags
     */
    List<String> getTags();

}
