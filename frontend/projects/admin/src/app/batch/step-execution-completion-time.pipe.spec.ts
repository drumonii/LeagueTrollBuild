import { StepExecutionCompletionTimePipe } from './step-execution-completion-time.pipe';
import { BatchStepExecution } from './batch-job';

describe('StepExecutionCompletionTimePipe', () => {
  const pipe = new StepExecutionCompletionTimePipe();

  it('should get StepExecution completion time from start and end time', () => {
    const stepExecution: BatchStepExecution = {
      id: 1,
      version: 2,
      name: 'itemsRetrievalJobStep',
      startTime: '2019-07-19T23:44:55.854',
      endTime: '2019-07-19T23:46:30.661',
      status: 'COMPLETED',
      commitCount: 0,
      readCount: 0,
      filterCount: 0,
      writeCount: 0,
      readSkipCount: 0,
      writeSkipCount: 0,
      processSkipCount: 0,
      rollbackCount: 0,
      exitCode: 'COMPLETED',
      exitMessage: '',
      lastUpdated: '2019-07-19T23:46:30.661'
    };
    expect(pipe.transform(stepExecution)).toBe('1.58 min');
  });

  it('should get StepExecution completion time with no end time', () => {
    const stepExecution: BatchStepExecution = {
      id: 1,
      version: 2,
      name: 'itemsRetrievalJobStep',
      startTime: '2019-07-19T23:44:55.854',
      endTime: '',
      status: 'STARTED',
      commitCount: 0,
      readCount: 0,
      filterCount: 0,
      writeCount: 0,
      readSkipCount: 0,
      writeSkipCount: 0,
      processSkipCount: 0,
      rollbackCount: 0,
      exitCode: '',
      exitMessage: '',
      lastUpdated: '2019-07-19T23:46:30.661'
    };
    expect(pipe.transform(stepExecution)).toBe('');
  });

  it('should not filter null StepExecution', () => {
    expect(pipe.transform(null)).toBe('');
  });
});
