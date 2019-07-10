package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class MapsRetrievalItemWriteListener implements ItemWriteListener<GameMap> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapsRetrievalItemWriteListener.class);

    @Override
    public void beforeWrite(List<? extends GameMap> gameMaps) {
        LOGGER.info("Preparing to write {} Maps", gameMaps.size());
    }

    @Override
    public void afterWrite(List<? extends GameMap> gameMaps) {
        LOGGER.info("Finished writing {} Maps", gameMaps.size());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends GameMap> gameMaps) {

    }

}
