package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class ItemsRetrievalItemReadListener implements ItemReadListener<Item> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsRetrievalItemReadListener.class);

    @Override
    public void beforeRead() {
        // nothing to do before read
    }

    @Override
    public void afterRead(Item item) {
        LOGGER.info("Finished reading Item: {}", item.getName());
    }

    @Override
    public void onReadError(Exception ex) {
        // nothing to do on read error
    }

}
