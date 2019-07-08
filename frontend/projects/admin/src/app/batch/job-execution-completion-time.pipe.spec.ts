import { JobExecutionCompletionTimePipe } from './job-execution-completion-time.pipe';
import { BatchJobExecution } from './batch-job';

describe('JobExecutionCompletionTimePipe', () => {
  const pipe = new JobExecutionCompletionTimePipe();

  it('should get BatchJobExecution completion time from start and end time', () => {
    const jobExecution: BatchJobExecution = {
      id: 1,
      version: 2,
      createTime: '2019-07-19T23:44:55.854',
      startTime: '2019-07-19T23:44:55.854',
      endTime: '2019-07-19T23:46:30.661',
      status: 'COMPLETED',
      exitCode: 'COMPLETED',
      exitMessage: '',
      lastUpdated: '2019-07-19T23:46:30.661'
    };
    expect(pipe.transform(jobExecution)).toBe('1.58 min');
  });

  it('should get BatchJobExecution completion time with no end time', () => {
    const jobExecution: BatchJobExecution = {
      id: 1,
      version: 2,
      createTime: '2019-07-19T23:44:55.854',
      startTime: '2019-07-19T23:44:55.854',
      endTime: '',
      status: 'STARTED',
      exitCode: '',
      exitMessage: '',
      lastUpdated: '2019-07-19T23:46:30.661'
    };
    expect(pipe.transform(jobExecution)).toBe('');
  });

  it('should not filter null JobExecution', () => {
    expect(pipe.transform(null)).toBe('');
  });
});
