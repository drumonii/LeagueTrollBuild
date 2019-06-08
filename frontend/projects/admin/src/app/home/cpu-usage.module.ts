import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CpuUsageComponent } from './cpu-usage.component';
import { CpuUsageService } from './cpu-usage.service';

@NgModule({
  imports: [
    CommonModule
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
