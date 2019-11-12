import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';

import { forkJoin, Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { AdminLogger } from '@admin-service/admin-logger.service';
import { ScheduledTasks } from '../actuator-scheduled-tasks-response';
import { BatchJobInstance } from '../../../batch/batch-job';
import { Paginated } from '../../../batch/datagrid';
import { MetricsResponse } from '../actuator-metric-response';

@Injectable()
export class AppHealthService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {}

  getAppHealth(): Observable<AppHealth> {
    return forkJoin([this.getFailedJobs(), this.getNextScheduledAllRetrievalsJob(), this.getUptime()])
      .pipe(
        map((appHealths) => {
          if (appHealths.some(appHealth => appHealth === null)) {
            return {
              failedJobs: null,
              nextScheduledAllRetrievalsJob: null,
              uptime: null
            };
          }
          return {
            failedJobs: appHealths[0],
            nextScheduledAllRetrievalsJob: appHealths[1],
            uptime: appHealths[2]
          };
        })
      );
  }

  getFailedJobs(daysInPast = 10): Observable<FailedJobs> {
    const params = new HttpParams()
      .set('jobExecution.status', 'FAILED')
      .append('sort', 'jobExecution.startTime,desc');
    const options = {
      params
    };
    return this.httpClient.get<Paginated<BatchJobInstance>>('/job-instances', options)
      .pipe(
        map((response) => response.content),
        map((jobInstances) => {
          const dateAgo = new Date();
          dateAgo.setDate(dateAgo.getDate() - daysInPast);
          return jobInstances.filter(jobInstance => new Date(jobInstance.jobExecution.startTime).getTime() >= dateAgo.getTime());
        }),
        map((jobInstances) => ({ count: jobInstances.length })),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /job-instances: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getNextScheduledAllRetrievalsJob(): Observable<Date> {
    return this.httpClient.get<ScheduledTasks>('/actuator/scheduledtasks')
      .pipe(
        map((scheduledTasks) => {
          const cronExpression = scheduledTasks.cron.find(cron => cron.runnable.target ===
            'com.drumonii.loltrollbuild.batch.scheduling.RetrievalJobsScheduling.runAllRetrievalsJob').expression;

          // Extremely rudimentary cron expression parsing
          const parts = cronExpression.trim().split(' ');
          const seconds = parts[0];
          const minutes = parts[1];
          const hours = parts[2];

          const now = new Date();

          const nextRunDate = new Date();
          nextRunDate.setSeconds(+seconds);
          nextRunDate.setMinutes(+minutes);
          nextRunDate.setHours(+hours);

          if (now.getHours() > nextRunDate.getHours()) {
            nextRunDate.setDate(nextRunDate.getDate() + 1);
          }

          return nextRunDate;
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /actuator/scheduledtasks: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getUptime(): Observable<Uptime> {
    return this.httpClient.get<MetricsResponse>('/actuator/metrics/process.uptime')
      .pipe(
        map((metrics) => {
          const seconds = Math.floor(metrics.measurements[0].value);

          const days = Math.floor(seconds / (3600 * 24));
          const hours = Math.floor(seconds % (3600 * 24) / 3600);
          const minutes = Math.floor(seconds % 3600 / 60);

          const daysDisplay = days > 0 ? days + (days === 1 ? ' day, ' : ' days, ') : '';
          const hoursDisplay = hours > 0 ? hours + (hours === 1 ? ' hour, ' : ' hours, ') : '';
          const minutesDisplay = minutes > 0 ? minutes + (minutes === 1 ? ' minute' : ' minutes') : '';

          return {
            total: daysDisplay + hoursDisplay + minutesDisplay,
            seconds
          };
        }),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /actuator/metrics/process.uptime: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

}

export interface AppHealth {
  failedJobs: FailedJobs;
  nextScheduledAllRetrievalsJob: Date;
  uptime: Uptime;
}

export interface FailedJobs {
  count: number;
}

export interface Uptime {
  total: string;
  seconds: number;
}
