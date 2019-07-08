import { Pipe, PipeTransform } from '@angular/core';

import { BatchJobExecution, getCompletionTime } from './batch-job';

@Pipe({
  name: 'jobExecutionCompletionTime'
})
export class JobExecutionCompletionTimePipe implements PipeTransform {

  transform(jobExecution: BatchJobExecution): string {
    if (!jobExecution || !jobExecution.endTime) {
      return '';
    }
    return getCompletionTime(jobExecution.startTime, jobExecution.endTime);
  }

}
