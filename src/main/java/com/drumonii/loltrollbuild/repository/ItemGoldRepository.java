package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.ItemGold;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Simple repository to the ITEM_GOLD table.
 */
@RepositoryRestResource(path = "gold")
public interface ItemGoldRepository extends Repository<ItemGold, Integer> {
}
