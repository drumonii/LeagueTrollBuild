export interface ScheduledTasks {
  cron: CronScheduledTask[];
}

interface CronScheduledTask {
  runnable: CronRunnable;
  expression: string;
}

interface CronRunnable {
  target: string;
}
