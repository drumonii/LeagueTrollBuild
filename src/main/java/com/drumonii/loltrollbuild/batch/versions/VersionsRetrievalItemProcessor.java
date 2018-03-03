package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.PassThroughItemProcessor;

/**
 * {@link ItemProcessor} for processing {@link Version}s from Riot's API.
 */
public class VersionsRetrievalItemProcessor extends PassThroughItemProcessor<Version> {

}
