import { Pipe, PipeTransform } from '@angular/core';

import { BatchStepExecution, getCompletionTime } from './batch-job';

@Pipe({
  name: 'stepExecutionCompletionTime'
})
export class StepExecutionCompletionTimePipe implements PipeTransform {

  transform(stepExecution: BatchStepExecution): string {
    if (!stepExecution || !stepExecution.endTime) {
      return '';
    }
    return getCompletionTime(stepExecution.startTime, stepExecution.endTime);
  }

}
