package com.drumonii.loltrollbuild.batch.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Simple scheduling for retrievals {@link Job}s.
 */
@Component
@Slf4j
public class RetrievalJobsScheduling {

	public static final String LATEST_PATCH_KEY = "latestRiotPatch";

	static final String CRON_SCHEDULE = "0 0 4 * * ?";

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("allRetrievalsJob")
	private Job allRetrievalsJob;

	/**
	 * Runs the allRetrievalsJob {@link Job} every day of every month at 4 AM. Only if there's a new patch will the
	 * job actually run.
	 */
	@Scheduled(cron = CRON_SCHEDULE)
	public void runAllRetrievalsJob() {
		try {
			jobLauncher.run(allRetrievalsJob, allRetrievalsJob.getJobParametersIncrementer().getNext(new JobParameters()));
		} catch (JobExecutionException e) {
			log.warn("Caught JobExecutionException while running all retrievals job", e);
		} catch (Exception e) {
			log.error("Caught exception while running all retrievals job", e);
		}
	}

}
