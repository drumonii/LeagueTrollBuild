package com.drumonii.loltrollbuild.batch.items;

import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

class ItemsRetrievalJobLauncherTestUtils extends JobLauncherTestUtils {

	@Autowired
	@Override
	public void setJob(@Qualifier("itemsRetrievalJob") Job itemsRetrievalJob) {
		super.setJob(itemsRetrievalJob);
	}

}
