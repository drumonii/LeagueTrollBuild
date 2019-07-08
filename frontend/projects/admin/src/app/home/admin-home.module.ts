import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminHomeRoutingModule } from './admin-home-routing.module';
import { AdminHomePage } from './admin-home.page';
import { CpuUsageModule } from './dashboard/cpu-usage.module';
import { FailedJobsModule } from './dashboard/failed-jobs.module';
import { MemoryUsageModule } from './dashboard/memory-usage.module';
import { GlobalErrorModule } from './dashboard/global-error.module';

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
