package com.drumonii.loltrollbuild.batch.scheduling;

import com.drumonii.loltrollbuild.riot.VersionsRetrieval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Simple scheduling for retrievals {@link Job}s.
 */
@Component
@Slf4j
public class RetrievalJobsScheduling {

	@Autowired
	private VersionsRetrieval versionsRetrieval;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("allRetrievalsJob")
	private Job allRetrievalsJob;

	/**
	 * Runs the allRetrievalsJob {@link Job} every day of every month at 4 AM. Only if there's a new patch will the
	 * job actually run.
	 */
	@Scheduled(cron = "0 0 4 * * ?")
	public void runAllRetrievalsJob() {
		try {
			jobLauncher.run(allRetrievalsJob, new JobParametersBuilder()
					.addString("latestRiotPatch",
							versionsRetrieval.latestVersion(versionsRetrieval.versionsFromResponse()).getPatch())
					.toJobParameters());
		} catch (JobInstanceAlreadyCompleteException e) {
			log.warn("Job instance was already completed with the latest Riot patch", e);
		} catch (Exception e) {
			log.error("Caught exception while running all retrievals job", e);
		}
	}

}
