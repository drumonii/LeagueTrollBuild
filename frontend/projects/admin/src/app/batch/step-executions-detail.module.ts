import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClrIconModule, ClrLoadingModule } from '@clr/angular';

import { StepExecutionsDetailComponent } from './step-executions-detail.component';
import { StepExecutionCompletionTimePipe } from './step-execution-completion-time.pipe';
import { StepExecutionsDetailService } from './step-executions-detail.service';

@NgModule({
  imports: [
    CommonModule,
    ClrIconModule,
    ClrLoadingModule
  ],
  declarations: [
    StepExecutionsDetailComponent,
    StepExecutionCompletionTimePipe
  ],
  exports: [
    StepExecutionsDetailComponent
  ],
  providers: [
    StepExecutionsDetailService
  ]
})
export class StepExecutionsDetailModule { }
