package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Champion;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Paging, sorting, and CRUD operations repository to the CHAMPION table.
 */
@CacheConfig(cacheNames = "champions")
public interface ChampionsRepository extends PagingAndSortingRepository<Champion, Integer> {

	/**
	 * Finds a {@link Champion} by its name.
	 *
	 * @param name the name to lookup (non case sensitive)
	 * @return a {@link Champion} from its name
	 */
	@Query("select c from Champion c where lower(c.name) = lower(:name) or lower(c.key) = lower(:name)")
	@RestResource(exported = false)
	Champion findByName(@Param("name") String name);

	/**
	 * Finds a {@link Page} of {@link Champion} from a search term by using {@code LIKE} for each searchable field.
	 *
	 * @param term the search term
	 * @param pageable {@link Pageable} for paging and sorting
	 * @return a {@link Page} of distinct {@link Champion} from a search term
	 */
	@Query("select c from Champion c " +
		   "where lower(c.key) like concat('%', lower(:term), '%') " +
		   "or lower(c.name) like concat('%', lower(:term), '%') " +
		   "or lower(c.title) like concat('%', lower(:term), '%') " +
		   "or exists (select t from c.tags t where lower(t) like concat('%', lower(:term), '%')) " +
		   "or lower(c.partype) = :term " +
		   "group by c.id")
	@RestResource(path = "find-by", rel = "find-by")
	Page<Champion> findBy(@Param("term") String term, Pageable pageable);

	/**
	 * Gets all possible distinct tags a {@link Champion} could have.
	 *
	 * @return the {@link List} of string tags
	 */
	@Query(value = "select distinct c.tag from champion_tag c", nativeQuery = true)
	List<String> getTags();

	@Cacheable
	@Override
	Iterable<Champion> findAll(Sort sort);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll();

}
