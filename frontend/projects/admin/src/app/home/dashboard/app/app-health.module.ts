import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClrAlertModule, ClrIconModule } from '@clr/angular';

import { AppHealthComponent } from './app-health.component';
import { AppHealthService } from './app-health.service';
import { NextScheduledFromNowPipe } from './next-scheduled-from-now.pipe';

@NgModule({
  imports: [
    CommonModule,
    ClrAlertModule,
    ClrIconModule
  ],
  declarations: [
    AppHealthComponent,
    NextScheduledFromNowPipe
  ],
  exports: [
    AppHealthComponent
  ],
  providers: [
    AppHealthService
  ]
})
export class AppHealthModule { }
