package com.drumonii.loltrollbuild.batch.champions;

import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

class ChampionsRetrievalJobLauncherTestUtils extends JobLauncherTestUtils {

	@Autowired
	@Override
	public void setJob(@Qualifier("championsRetrievalJob") Job championsRetrievalJob) {
		super.setJob(championsRetrievalJob);
	}

}
