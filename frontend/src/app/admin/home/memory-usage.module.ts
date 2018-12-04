import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MemoryUsageComponent } from './memory-usage.component';
import { MemoryUsageService } from './memory-usage.service';

@NgModule({
  imports: [
    CommonModule
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
