import { BatchJobExecution } from './batch-job-execution';

export interface BatchJobInstance {
  id: number;
  version: number;
  name: string;
  key: string;
  jobExecution: BatchJobExecution;
}
