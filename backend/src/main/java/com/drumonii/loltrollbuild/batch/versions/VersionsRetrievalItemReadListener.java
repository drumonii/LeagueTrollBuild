package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class VersionsRetrievalItemReadListener implements ItemReadListener<Version> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionsRetrievalItemReadListener.class);

    @Override
    public void beforeRead() {
        // nothing to do before read
    }

    @Override
    public void afterRead(Version version) {
        LOGGER.info("Finished reading Version: {}", version.getPatch());
    }

    @Override
    public void onReadError(Exception ex) {
        // nothing to do on read error
    }

}
