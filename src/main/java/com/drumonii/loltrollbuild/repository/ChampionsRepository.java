package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Champion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

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

	/**
	 * Finds a {@link List} of {@link Champion} by a name using {@code LIKE}.
	 *
	 * @param name the name to lookup (non case sensitive)
	 * @return a {@link List} of {@code LIKE} {@link Champion}s from a name
	 */
	@Query("select c from Champion c where lower(c.name) like concat('%', lower(:name), '%') " +
		   "or lower(c.key) like concat('%', lower(:name), '%')")
	@RestResource(path = "find-by-like-name", rel = "find-by-like-name")
	List<Champion> findByLikeName(@Param("name") String name);

}
