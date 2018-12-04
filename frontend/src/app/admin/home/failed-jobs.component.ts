import { Component, OnInit } from '@angular/core';
import { FailedJobsService } from './failed-jobs.service';

import { forkJoin, Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'ltb-admin-failed-jobs',
  templateUrl: './failed-jobs.component.html',
  styleUrls: ['./failed-jobs.component.scss']
})
export class FailedJobsComponent implements OnInit {

  failedJobs$: Observable<any>;
  gettingFailedJobs: boolean;

  constructor(private service: FailedJobsService) {}

  ngOnInit() {
    this.getFailedJobs();
  }

  getFailedJobs(): void {
    this.gettingFailedJobs = true;
    this.failedJobs$ = forkJoin(this.service.getFailedJobs())
      .pipe(
        finalize(() => this.gettingFailedJobs = false)
      );
  }

  getFailedJobsClass(failedJobs: number): string {
    if (failedJobs > 0) {
      return 'has-text-danger';
    }
    return 'has-text-black';
  }

}
