package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * CRUD operations repository to the ITEM table.
 */
public interface ItemsRepository extends CrudRepository<Item, Integer> {

	/**
	 * Gets a {@link List} of upgraded {@link Item} boots from {@code Boots of Speed} not including boot enchantments.
	 *
	 * @return a {@link List} of upgraded {@link Item} boots without enchantments
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Boots_of_Speed">Boots of Speed</a>
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Advanced_item">Advanced Items</a>
	 */
	@Query("select i from Item i join i.from f " +
		   "where i.id <> 1001 and i.name not like 'Enchantment%' and f in ('1001')")
	List<Item> boots();

	/**
	 * Gets a {@link List} of basic Trinket {@link Item}s (non Advanced) found only on Summoner's Rift.
	 *
	 * @return a {@link List} of basic Trinket {@link Item}s
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Trinket">Trinket</a>
	 */
	@Query("select i from Item i left join i.maps m " +
		   "where i.name like '%Trinket%' and i.gold.total = 0 and i.gold.purchasable = true " +
		   "and (m is null or key(m) <> '1') " +
		   "group by i.id")
	List<Item> trinkets();

	/**
	 * Gets a {@link List} of Viktor only starting {@link Item}s.
	 *
	 * @return a {@link List} of Viktor only {@link Item}s
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Viktor">Viktor</a>
	 */
	@Query("select i from Item i where i.requiredChampion = 'Viktor'")
	@RestResource(path = "viktor-only", rel = "viktor-only")
	List<Item> viktorOnly();

	/**
	 * Gets a {@link List} of {@link Item}s eligible for the troll build. That is, all purchasable (excluding items like
	 * Muramana or Seraph's Embrace - they are non purchasable), non-consumable, and fully upgraded items found only on
	 * Summoner's Rift. Excludes boots, Trinkets, items not requiring a particular champion, Crystalline Flask, jungle
	 * related items, Doran's items, and stackable items upon champion kills/assists.
	 *
	 * @return a {@link List} of {@link Item}s eligible for the troll build
	 */
	@Query("select i from Item i left join i.into i_into left outer join i.maps m " +
		   "where (m is null or not exists (select m2 from i.maps m2 where key(m2) = '1')) " +
		   "and i.gold.purchasable = true and i.consumed is null and i_into is null " +
		   "and i.id <> 1001 and i.description not like '%Enchants boots%' " +
		   "and i.name not like '%Trinket%' " +
		   "and i.requiredChampion is null and i.id <> 2041 " +
		   "and i.name not like 'Enchantment%' and i.name not like 'Doran%' " +
		   "and i.description not like '%At 20 stacks%' " +
		   "group by i.id")
	@RestResource(path = "for-troll-build", rel = "for-troll-build")
	List<Item> forTrollBuild();

}
