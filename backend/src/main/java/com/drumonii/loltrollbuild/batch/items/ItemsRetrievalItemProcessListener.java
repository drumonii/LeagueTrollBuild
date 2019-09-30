package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

public class ItemsRetrievalItemProcessListener implements ItemProcessListener<Item, Item> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsRetrievalItemProcessListener.class);

    @Override
    public void beforeProcess(Item item) {
        LOGGER.info("Processing Item: {}", item.getName());
    }

    @Override
    public void afterProcess(Item item, Item result) {
        if (result == null) {
            LOGGER.info("Ignoring Item: {} due to no changes from previous patch", item.getName());
        }
    }

    @Override
    public void onProcessError(Item item, Exception e) {
        // nothing to do on process error
    }

}
