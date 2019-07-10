package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class ChampionsRetrievalItemWriteListener implements ItemWriteListener<Champion> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChampionsRetrievalItemWriteListener.class);

    @Override
    public void beforeWrite(List<? extends Champion> champions) {
        LOGGER.info("Preparing to write {} Champions", champions.size());
    }

    @Override
    public void afterWrite(List<? extends Champion> champions) {
        LOGGER.info("Finished writing {} Champions", champions.size());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends Champion> items) {

    }

}
