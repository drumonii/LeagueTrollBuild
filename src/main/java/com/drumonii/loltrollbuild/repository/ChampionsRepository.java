package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Champion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * CRUD operations repository to the CHAMPION table.
 */
public interface ChampionsRepository extends CrudRepository<Champion, Integer> {

	/**
	 * Finds a {@link Champion} by its name.
	 *
	 * @param name the name to lookup (non case sensitive)
	 * @return a {@link Champion} from its name
	 */
	@Query("select c from Champion c where lower(c.name) = lower(:name) or lower(c.key) = lower(:name)")
	@RestResource(path = "find-by-name", rel = "find-by-name")
	Champion findByName(@Param("name") String name);

}
