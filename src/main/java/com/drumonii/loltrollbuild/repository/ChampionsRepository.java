package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Champion;
import org.springframework.data.repository.CrudRepository;

/**
 * CRUD operations repository to the CHAMPION table.
 */
public interface ChampionsRepository extends CrudRepository<Champion, Integer> {
}
