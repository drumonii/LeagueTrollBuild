import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClrAlertModule, ClrIconModule } from '@clr/angular';

import { MemoryUsageComponent } from './memory-usage.component';
import { MemoryUsageService } from './memory-usage.service';

@NgModule({
  imports: [
    CommonModule,
    ClrAlertModule,
    ClrIconModule
  ],
  declarations: [
    MemoryUsageComponent
  ],
  exports: [
    MemoryUsageComponent
  ],
  providers: [
    MemoryUsageService
  ]
})
export class MemoryUsageModule { }
