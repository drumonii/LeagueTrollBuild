import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClrAlertModule, ClrIconModule } from '@clr/angular';

import { GlobalErrorComponent } from './global-error.component';
import { GlobalErrorService } from './global-error.service';

@NgModule({
  imports: [
    CommonModule,
    ClrAlertModule,
    ClrIconModule
  ],
  declarations: [
    GlobalErrorComponent
  ],
  exports: [
    GlobalErrorComponent
  ],
  providers: [
    GlobalErrorService
  ]
})
export class GlobalErrorModule { }
