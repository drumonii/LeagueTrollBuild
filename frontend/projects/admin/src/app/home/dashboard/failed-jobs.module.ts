import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClrAlertModule, ClrIconModule } from '@clr/angular';

import { FailedJobsComponent } from './failed-jobs.component';
import { FailedJobsService } from './failed-jobs.service';

@NgModule({
  imports: [
    CommonModule,
    ClrAlertModule,
    ClrIconModule
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
