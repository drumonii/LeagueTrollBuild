package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;

import java.util.List;

/**
 * {@link ItemReader} for reading {@link Version}s from Riot's API.
 */
public class VersionsRetrievalItemReader extends ListItemReader<Version> {

	public VersionsRetrievalItemReader(List<Version> list) {
		super(list);
	}

}
