package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

public class MapsRetrievalItemProcessListener implements ItemProcessListener<GameMap, GameMap> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapsRetrievalItemProcessListener.class);

    @Override
    public void beforeProcess(GameMap gameMap) {
        LOGGER.info("Processing Map: {}", gameMap.getMapName());
    }

    @Override
    public void afterProcess(GameMap gameMap, GameMap result) {
        if (result == null) {
            LOGGER.info("Ignoring Map: {} due to no changes from previous patch", gameMap.getMapName());
        }
    }

    @Override
    public void onProcessError(GameMap gameMap, Exception e) {
        // nothing to do on process error
    }

}
