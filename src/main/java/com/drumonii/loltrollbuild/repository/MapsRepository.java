package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.GameMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Paging, sorting, and CRUD operations repository to the MAP table.
 */
@RepositoryRestResource(path = "maps", collectionResourceRel = "maps")
public interface MapsRepository extends PagingAndSortingRepository<GameMap, Integer> {

	/**
	 * Finds a {@link Page} of {@link GameMap} from a search term by using {@code LIKE} for each searchable field.
	 *
	 * @param term the search term
	 * @param pageable {@link Pageable} for paging and sorting
	 * @return a {@link Page} of distinct {@link GameMap} from a search term
	 */
	@Query("select m from GameMap m " +
		   "where lower(m.mapName) like concat('%', lower(:term), '%') " +
		   "group by m.id")
	@RestResource(path = "find-by", rel = "find-by")
	Page<GameMap> findBy(@Param("term") String term, Pageable pageable);

}
