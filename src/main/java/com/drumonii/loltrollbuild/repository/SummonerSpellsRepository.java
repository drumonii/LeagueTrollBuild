package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Paging, sorting, and CRUD operations repository to the SUMMONER_SPELL table.
 */
@RepositoryRestResource(path = "summoner-spells")
@CacheConfig(cacheNames = "summonerSpells")
public interface SummonerSpellsRepository extends PagingAndSortingRepository<SummonerSpell, Integer> {

	/**
	 * Gets a {@link List} of {@link SummonerSpell}s eligible for the troll build based on a game mode.
	 *
	 * @param mode the game mode to search
	 * @return a {@link List} of {@link SummonerSpell}s eligible for the troll build
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Category:Game_modes">Game Modes</a>
	 */
	@Query("select s from SummonerSpell s join s.modes m " +
		   "where m in (:mode) " +
	       "group by s.id")
	@RestResource(path = "for-troll-build", rel = "for-troll-build")
	@Cacheable
	List<SummonerSpell> forTrollBuild(@Param("mode") GameMode mode);

	/**
	 * Finds a {@link Page} of {@link SummonerSpell} from a search term by using {@code LIKE} for each searchable field.
	 *
	 * @param term the search term
	 * @param pageable {@link Pageable} for paging and sorting
	 * @return a {@link Page} of distinct {@link SummonerSpell} from a search term
	 */
	@Query("select s from SummonerSpell s " +
		   "where lower(s.name) like concat('%', lower(:term), '%') " +
		   "or exists (select m from s.modes m where lower(m) like concat('%', lower(:term), '%')) " +
		   "group by s.id")
	@RestResource(path = "find-by", rel = "find-by")
	Page<SummonerSpell> findBy(@Param("term") String term, Pageable pageable);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll();

}
