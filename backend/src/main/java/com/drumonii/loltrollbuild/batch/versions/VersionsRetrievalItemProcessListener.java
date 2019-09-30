package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

public class VersionsRetrievalItemProcessListener implements ItemProcessListener<Version, Version> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionsRetrievalItemProcessListener.class);

    @Override
    public void beforeProcess(Version item) {
        LOGGER.info("Processing Version: {}", item.getPatch());
    }

    @Override
    public void afterProcess(Version version, Version result) {
        if (result == null) {
            LOGGER.info("Ignoring Version: {} due to no changes from previous patch", version.getPatch());
        }
    }

    @Override
    public void onProcessError(Version version, Exception e) {
        // nothing to do on process error
    }

}
