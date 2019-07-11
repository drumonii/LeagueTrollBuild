import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminHomeRoutingModule } from './admin-home-routing.module';
import { AdminHomePage } from './admin-home.page';
import { CpuUsageModule } from './cpu-usage.module';
import { FailedJobsModule } from './failed-jobs.module';
import { MemoryUsageModule } from './memory-usage.module';
import { GlobalErrorModule } from './global-error.module';

@NgModule({
  imports: [
    CommonModule,
    AdminHomeRoutingModule,
    CpuUsageModule,
    FailedJobsModule,
    MemoryUsageModule,
    GlobalErrorModule
  ],
  declarations: [
    AdminHomePage
  ]
})
export class AdminHomeModule { }