package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class VersionsRetrievalItemWriteListener implements ItemWriteListener<Version> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionsRetrievalItemWriteListener.class);

    @Override
    public void beforeWrite(List<? extends Version> versions) {
        LOGGER.info("Preparing to write {} Versions", versions.size());
    }

    @Override
    public void afterWrite(List<? extends Version> versions) {
        LOGGER.info("Finished writing {} Versions", versions.size());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends Version> versions) {

    }

}
