package com.drumonii.loltrollbuild.batch.maps;

import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MapsRetrievalJobLauncherTestUtils extends JobLauncherTestUtils {

	@Autowired
	@Override
	public void setJob(@Qualifier("mapsRetrievalJob") Job mapsRetrievalJob) {
		super.setJob(mapsRetrievalJob);
	}

}
