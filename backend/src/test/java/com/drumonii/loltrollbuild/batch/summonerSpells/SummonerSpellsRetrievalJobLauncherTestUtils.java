package com.drumonii.loltrollbuild.batch.summonerSpells;

import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class SummonerSpellsRetrievalJobLauncherTestUtils extends JobLauncherTestUtils {

	@Autowired
	@Override
	public void setJob(@Qualifier("summonerSpellsRetrievalJob") Job summonerSpellsRetrievalJob) {
		super.setJob(summonerSpellsRetrievalJob);
	}

}
