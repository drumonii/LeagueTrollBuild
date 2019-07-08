import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClrAlertModule, ClrDatagridModule, ClrIconModule, ClrLoadingButtonModule, ClrLoadingModule, ClrPopoverModule } from '@clr/angular';

import { BatchJobDatagridComponent } from './batch-job-datagrid.component';
import { StepExecutionsDetailModule } from './step-executions-detail.module';
import { JobExecutionCompletionTimePipe } from './job-execution-completion-time.pipe';
import { BatchJobDatagridService } from './batch-job-datagrid.service';

@NgModule({
  imports: [
    CommonModule,
    ClrAlertModule,
    ClrDatagridModule,
    ClrIconModule,
    ClrLoadingButtonModule,
    ClrLoadingModule,
    ClrPopoverModule,
    StepExecutionsDetailModule
  ],
  declarations: [
    BatchJobDatagridComponent,
    JobExecutionCompletionTimePipe
  ],
  exports: [
    BatchJobDatagridComponent
  ],
  providers: [
    BatchJobDatagridService
  ]
})
export class BatchJobDatagridModule { }
