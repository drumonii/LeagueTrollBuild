package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

public class ChampionsRetrievalItemProcessListener implements ItemProcessListener<Champion, Champion> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChampionsRetrievalItemProcessListener.class);

    @Override
    public void beforeProcess(Champion champion) {
        LOGGER.info("Processing Champion: {}", champion.getName());
    }

    @Override
    public void afterProcess(Champion champion, Champion result) {
        if (result == null) {
            LOGGER.info("Ignoring Champion: {} due to no changes from previous patch", champion.getName());
        }
    }

    @Override
    public void onProcessError(Champion champion, Exception e) {
        // nothing to do on process error
    }

}
