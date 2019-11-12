import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClrAlertModule, ClrIconModule } from '@clr/angular';

import { EnvComponent } from './env.component';
import { EnvService } from './env.service';

@NgModule({
  imports: [
    CommonModule,
    ClrAlertModule,
    ClrIconModule
  ],
  declarations: [
    EnvComponent
  ],
  exports: [
    EnvComponent
  ],
  providers: [
    EnvService
  ]
})
export class EnvModule { }
