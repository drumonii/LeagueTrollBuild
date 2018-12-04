import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FailedJobsComponent } from './failed-jobs.component';
import { FailedJobsService } from './failed-jobs.service';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    FailedJobsComponent
  ],
  exports: [
    FailedJobsComponent
  ],
  providers: [
    FailedJobsService
  ]
})
export class FailedJobsModule { }
