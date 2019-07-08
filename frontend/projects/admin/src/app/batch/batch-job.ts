export interface BatchJobInstance {
  id: number;
  version: number;
  name: string;
  key: string;
  jobExecution: BatchJobExecution;
}

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

export function getCompletionTime(startTime: string, endTime: string) {
  const startDate = new Date(startTime);
  const endDate = new Date(endTime);
  const diffInMs = endDate.getTime() - startDate.getTime();
  const diffInMinutes = (diffInMs / 1000) / 60;
  return `${diffInMinutes.toFixed(2)} min`;
}
