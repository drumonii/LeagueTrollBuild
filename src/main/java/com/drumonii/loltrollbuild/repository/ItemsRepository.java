package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Item;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Paging, sorting, and CRUD operations repository to the ITEM table.
 */
@CacheConfig(cacheNames = "items")
public interface ItemsRepository extends JpaRepository<Item, Integer> {

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
	@RestResource(exported = false)
	List<Item> boots(@Param("mapId") String mapId);

	/**
	 * Gets a {@link List} of basic Trinket {@link Item}s (non Advanced) found only on the specified {@link GameMap}.
	 *
	 * @param mapId the {@link GameMap}'s ID
	 * @return a {@link List} of basic Trinket {@link Item}s
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Trinket">Trinket</a>
	 */
	@Query("select i from Item i left join i.maps m " +
		   "where i.name like '%Trinket%' and i.gold.total = 0 and i.gold.purchasable = true " +
		   "and (key(m) <> :mapId and m = false) " +
		   "group by i.id")
	@RestResource(exported = false)
	List<Item> trinkets(@Param("mapId") String mapId);

	/**
	 * Gets a {@link List} of Viktor only starting {@link Item}s.
	 *
	 * @return a {@link List} of Viktor only {@link Item}s
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Viktor">Viktor</a>
	 */
	@Query("select i from Item i where i.requiredChampion = 'Viktor'")
	@RestResource(exported = false)
	List<Item> viktorOnly();

	/**
	 * Gets a {@link List} of {@link Item}s eligible for the troll build. That is, all purchasable (excluding items like
	 * Muramana or Seraph's Embrace - they are non purchasable), non-consumable, and fully upgraded items found only on
	 * the specified {@link GameMap}. This excludes boots, Trinkets, items not requiring a particular champion, jungle
	 * related items, and Doran's items.
	 *
	 * @param mapId the {@link GameMap}'s ID
	 * @return a {@link List} of {@link Item}s eligible for the troll build
	 */
	@Query("select i from Item i left join i.into i_into left join i.maps m " +
		   "where i.gold.purchasable = true and i.consumed is null and (i.group is null or i.group <> 'FlaskGroup') " +
		   "and i_into is null and not exists (select m2 from i.maps m2 where key(m2) = :mapId and m2 = false)" +
		   "and i.id <> 1001 and i.description not like '%Enchants boots%' " +
		   "and (i.group is null or i.group <> 'RelicBase') " +
		   "and i.requiredChampion is null " +
		   "and i.name not like 'Enchantment%' and i.name not like 'Doran%' " +
		   "group by i.id")
	@RestResource(exported = false)
	@Cacheable
	List<Item> forTrollBuild(@Param("mapId") String mapId);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll();

}
