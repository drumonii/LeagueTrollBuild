package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class ItemsRetrievalItemWriteListener implements ItemWriteListener<Item> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsRetrievalItemWriteListener.class);

    @Override
    public void beforeWrite(List<? extends Item> items) {
        LOGGER.info("Preparing to write {} Items", items.size());
    }

    @Override
    public void afterWrite(List<? extends Item> items) {
        LOGGER.info("Finished writing {} Items", items.size());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends Item> items) {

    }

}
