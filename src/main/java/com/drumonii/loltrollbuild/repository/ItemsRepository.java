package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Item;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository to the ITEM table.
 */
@CacheConfig(cacheNames = "items")
public interface ItemsRepository extends JpaRepository<Item, Integer>, JpaSpecificationExecutor<Item> {

	/**
	 * Gets a {@link List} of upgraded {@link Item} boots from Boots of Speed found only on the specified
	 * {@link GameMap}.
	 *
	 * @param mapId the {@link GameMap}'s ID
	 * @return a {@link List} of upgraded {@link Item} boots
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Boots_of_Speed">Boots of Speed</a>
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Advanced_item">Advanced Items</a>
	 */
	@Query("select i from Item i join i.from f left join i.maps m " +
		   "where i.id <> 1001 and f in ('1001') " +
		   "and (key(m) <> :mapId and m = false) " +
		   "group by i.id")
	@Cacheable(key = "{#root.methodName, #mapId}")
	List<Item> boots(@Param("mapId") int mapId);

	/**
	 * Gets a {@link List} of basic Trinket {@link Item}s (non Advanced) found only on the specified {@link GameMap}.
	 *
	 * @param mapId the {@link GameMap}'s ID
	 * @return a {@link List} of basic Trinket {@link Item}s
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Trinket">Trinket</a>
	 */
	@Query("select i from Item i left join i.maps m " +
		   "where (i.name like '%Trinket%' or i.description like '%Trinket%') " +
		   "and i.gold.total = 0 and i.gold.purchasable = true " +
	       "and (key(m) <> :mapId and m = false) " +
		   "group by i.id")
	@Cacheable(key = "{#root.methodName, #mapId}")
	List<Item> trinkets(@Param("mapId") int mapId);

	/**
	 * Gets a {@link List} of Viktor only starting {@link Item}s.
	 *
	 * @return a {@link List} of Viktor only {@link Item}s
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Viktor">Viktor</a>
	 */
	@Query("select i from Item i where i.requiredChampion = 'Viktor'")
	@Cacheable(key = "#root.methodName")
	List<Item> viktorOnly();

	/**
	 * Gets a {@link List} of {@link Item}s eligible for the troll build. That is, all purchasable (excluding items like
	 * Muramana or Seraph's Embrace - they are non purchasable), non-consumable, and fully upgraded items found only on
	 * the specified {@link GameMap}. This excludes boots, potions, Trinkets, items not requiring a particular champion,
	 * items not requiring an ally champion, jungle related items, Doran's items, and Quick Charge items.
	 *
	 * @param mapId the {@link GameMap}'s ID
	 * @return a {@link List} of {@link Item}s eligible for the troll build
	 */
	@Query("select i from Item i left join i.into i_into join i.maps m " +
		   "where i.name is not null and i.description is not null " +
		   "and i.gold.purchasable = true and i.consumed is null and (i.group is null or i.group <> 'FlaskGroup') " +
		   "and i_into is null and key(m) = :mapId and m = true " +
		   "and i.id <> 1001 and i.description not like '%Movement%' " +
		   "and (i.name not like '%Potion%' and i.description not like '%Potion%') " +
		   "and (i.name not like '%Trinket%' and i.description not like '%Trinket%') " +
		   "and i.requiredAlly is null " +
		   "and i.requiredChampion is null " +
		   "and i.name not like 'Enchantment%' and i.name not like 'Doran%' and i.name not like '%(Quick Charge)'")
	@Cacheable(key = "{#root.methodName, #mapId}")
	List<Item> forTrollBuild(@Param("mapId") int mapId);

	@Cacheable
	@Override
	List<Item> findAll();

	@CacheEvict(allEntries = true)
	@Override
	<S extends Item> List<S> saveAll(Iterable<S> entities);

	@CacheEvict(allEntries = true)
	@Override
	<S extends Item> S save(S entity);

	@Cacheable
	@Override
	Optional<Item> findById(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void deleteById(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll(Iterable<? extends Item> entities);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll();

	@Cacheable(key = "{#spec.example.probe, #sort}")
	@Override
	List<Item> findAll(Specification<Item> spec, Sort sort);

}
