package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Item;
import org.springframework.data.repository.CrudRepository;

/**
 * CRUD operations repository to the ITEM table.
 */
public interface ItemsRepository extends CrudRepository<Item, Integer> {
}
