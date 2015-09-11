package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

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

}
