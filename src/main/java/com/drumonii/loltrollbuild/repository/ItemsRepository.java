package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Paging, sorting, and CRUD operations repository to the ITEM table.
 */
public interface ItemsRepository extends PagingAndSortingRepository<Item, Integer> {

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
		   "and (key(m) <> '11' and m = false) " +
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
	 * (New) Summoner's Rift. This excludes boots, Trinkets, items not requiring a particular champion, jungle related
	 * items, and Doran's items.
	 *
	 * @return a {@link List} of {@link Item}s eligible for the troll build
	 */
	@Query("select i from Item i left join i.into i_into left join i.maps m " +
		   "where i.gold.purchasable = true and i.consumed is null and (i.group is null or i.group <> 'FlaskGroup') " +
		   "and i_into is null and not exists (select m2 from i.maps m2 where key(m2) = '11' and m2 = false)" +
		   "and i.id <> 1001 and i.description not like '%Enchants boots%' " +
		   "and (i.group is null or i.group <> 'RelicBase') " +
		   "and i.requiredChampion is null " +
		   "and i.name not like 'Enchantment%' and i.name not like 'Doran%' " +
		   "group by i.id")
	@RestResource(path = "for-troll-build", rel = "for-troll-build")
	List<Item> forTrollBuild();

	/**
	 * Finds a {@link Page} of {@link Item} from a search term by using {@code LIKE} for each searchable field.
	 *
	 * @param term the search term
	 * @param pageable {@link Pageable} for paging and sorting
	 * @return a {@link Page} of distinct {@link Item} from a search term
	 */
	@Query("select i from Item i " +
		   "where lower(i.name) like concat('%', lower(:term), '%') " +
		   "or lower(i.group) like concat('%', lower(:term), '%') " +
		   "or lower(i.requiredChampion) like concat('%', lower(:term), '%') " +
		   "group by i.id")
	@RestResource(path = "find-by", rel = "find-by")
	Page<Item> findBy(@Param("term") String term, Pageable pageable);

}
