export interface BatchStepExecution {
  id: number;
  version: number;
  name: string;
  startTime: string;
  endTime: string;
  status: string;
  exitCode: string;
  exitMessage: string;
  lastUpdated: string;
  commitCount: number;
  readCount: number;
  filterCount: number;
  writeCount: number;
  readSkipCount: number;
  writeSkipCount: number;
  processSkipCount: number;
  rollbackCount: number;
}
