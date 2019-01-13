import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GlobalErrorComponent } from './global-error.component';
import { GlobalErrorService } from './global-error.service';

@NgModule({
  imports: [
    CommonModule
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
