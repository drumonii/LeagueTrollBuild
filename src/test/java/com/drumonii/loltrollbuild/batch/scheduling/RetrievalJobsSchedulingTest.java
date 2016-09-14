package com.drumonii.loltrollbuild.batch.scheduling;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Test;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class RetrievalJobsSchedulingTest extends BaseSpringTestRunner {

	@Test
	public void allRetrievalsCronExpression() throws Exception {
		CronTrigger cronTrigger = new CronTrigger("0 0 4 * * ?");
		Date triggerDate = Date.from(LocalDateTime.now().withHour(4).withMinute(0).withSecond(0).withNano(0)
				.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime nextExecutionTime = LocalDateTime.ofInstant(cronTrigger.nextExecutionTime(new
				SimpleTriggerContext(triggerDate, triggerDate, triggerDate)).toInstant(), ZoneId.systemDefault());
		assertThat(nextExecutionTime)
				.isEqualTo(LocalDateTime.now().plusDays(1).withHour(4).withMinute(0).withSecond(0).withNano(0));
	}

}