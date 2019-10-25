package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * API {@code @Service} for {@link Version}s.
 */
public interface VersionsApiService {

    /**
     * Queries by example from the given {@link Example} of {@link Version}.
     *
     * @param example the QBE {@link Example}
     * @param sort the {@link Sort}
     * @return the {@link List} of {@link Version}
     */
    List<Version> qbe(Example<Version> example, Sort sort);

    /**
     * Finds the {@link Version} using the given patch.
     *
     * @param patch the patch
     * @return the {@link Optional} {@link Version}
     */
    Optional<Version> findById(String patch);

    /**
     * Gets the latest saved {@link Version}, if exists.
     *
     * @return the latest {@link Version}, otherwise {@code null}
     */
    Version latestVersion();

}
