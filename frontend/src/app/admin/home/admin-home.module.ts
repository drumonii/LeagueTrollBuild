import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminHomeRoutingModule } from './admin-home-routing.module';

import { AdminHomePage } from './admin-home.page';
import { CpuUsageComponent } from './cpu-usage.component';
import { FailedJobsComponent } from './failed-jobs.component';
import { MemoryUsageComponent } from './memory-usage.component';
import { ServletErrorComponent } from './servlet-error.component';
import { AdminHomeService } from './admin-home.service';

@NgModule({
  imports: [
    CommonModule,
    AdminHomeRoutingModule
  ],
  declarations: [
    AdminHomePage,
    CpuUsageComponent,
    FailedJobsComponent,
    MemoryUsageComponent,
    ServletErrorComponent
  ],
  providers: [
    AdminHomeService
  ]
})
export class AdminHomeModule { }
