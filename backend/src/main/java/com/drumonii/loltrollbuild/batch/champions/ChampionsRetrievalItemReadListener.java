package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class ChampionsRetrievalItemReadListener implements ItemReadListener<Champion> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChampionsRetrievalItemReadListener.class);

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(Champion champion) {
        LOGGER.info("Finished reading Champion: {}", champion.getName());
    }

    @Override
    public void onReadError(Exception ex) {

    }

}
