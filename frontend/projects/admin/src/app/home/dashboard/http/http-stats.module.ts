import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClrAlertModule, ClrIconModule } from '@clr/angular';

import { HttpStatsComponent } from './http-stats.component';
import { HttpStatsService } from './http-stats.service';

@NgModule({
  imports: [
    CommonModule,
    ClrAlertModule,
    ClrIconModule
  ],
  declarations: [
    HttpStatsComponent
  ],
  exports: [
    HttpStatsComponent
  ],
  providers: [
    HttpStatsService
  ]
})
export class HttpStatsModule { }
