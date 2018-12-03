import { BatchStepExecution } from '@admin-model/batch-step-execution';

export interface BatchJobExecution {
  id: number;
  version: number;
  createTime: string;
  startTime: string;
  endTime: string;
  status: string;
  exitCode: string;
  exitMessage: string;
  lastUpdated: string;
  configurationLocation?: string;
  stepExecutions?: BatchStepExecution[];
}
