import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClrAlertModule, ClrIconModule } from '@clr/angular';

import { CpuUsageComponent } from './cpu-usage.component';
import { CpuUsageService } from './cpu-usage.service';

@NgModule({
  imports: [
    CommonModule,
    ClrAlertModule,
    ClrIconModule
  ],
  declarations: [
    CpuUsageComponent
  ],
  exports: [
    CpuUsageComponent
  ],
  providers: [
    CpuUsageService
  ]
})
export class CpuUsageModule { }
