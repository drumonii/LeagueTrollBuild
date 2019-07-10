package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class MapsRetrievalItemReadListener implements ItemReadListener<GameMap> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapsRetrievalItemReadListener.class);

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(GameMap gameMap) {
        LOGGER.info("Finished reading Map: {}", gameMap.getMapName());
    }

    @Override
    public void onReadError(Exception ex) {

    }

}
