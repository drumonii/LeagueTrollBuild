package com.drumonii.loltrollbuild.batch.versions;

import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class VersionsRetrievalJobLauncherTestUtils extends JobLauncherTestUtils {

	@Autowired
	@Override
	public void setJob(@Qualifier("versionsRetrievalJob") Job versionsRetrievalJob) {
		super.setJob(versionsRetrievalJob);
	}

}
